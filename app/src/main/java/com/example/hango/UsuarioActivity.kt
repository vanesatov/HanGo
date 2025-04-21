package com.example.hango

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.databinding.ActivityUsuarioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUsuarioBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val usuarioActual = auth.currentUser
        usuarioActual?.let { user ->
            binding.txtEmailUsuario.text = "Email: ${user.email}"

            val uid = user.uid
            database.child("usuarios").child(uid).child("nivel").get()
                .addOnSuccessListener { snapshot ->
                    val nivel = snapshot.getValue(Int::class.java) ?: 0
                    binding.txtNivelUsuario.text = "Nivel: $nivel"
                }
                .addOnFailureListener {
                    binding.txtNivelUsuario.text = "Nivel: No disponible"
                }
        }

        binding.btnCerrarSesion.setOnClickListener {
            auth.signOut()

            val prefs: SharedPreferences = getSharedPreferences("HanGoPrefs", MODE_PRIVATE)
            val editor = prefs.edit()
            if (!prefs.getBoolean("recordar", false)) {
                editor.clear()
                editor.apply()
            }
            Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()


            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
        }
        binding.btnUsuario.setCardBackgroundColor(ContextCompat.getColor(this, R.color.activo))
        binding.btnHome.setCardBackgroundColor(ContextCompat.getColor(this, R.color.inactivo))

        binding.btnUsuario.cardElevation = 4f
        binding.btnHome.cardElevation = 10f
    }
}









