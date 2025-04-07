package com.example.hango

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.databinding.ActivityUsuarioBinding
import com.google.firebase.auth.FirebaseAuth

class UsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUsuarioBinding
    private lateinit var auth: FirebaseAuth

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
        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        val usuarioActual = FirebaseAuth.getInstance().currentUser
        usuarioActual?.let {
            binding.txtEmailUsuario.text = "Email: ${it.email}"
        }

        // Cerrar sesión
        binding.btnCerrarSesion.setOnClickListener {
            auth.signOut()

            // Limpiar preferencias si no se marcó "Recuérdame"
            val prefs: SharedPreferences = getSharedPreferences("HanGoPrefs", MODE_PRIVATE)
            val editor = prefs.edit()
            if (!prefs.getBoolean("recordar", false)) {
                editor.clear()
                editor.apply()
            }
            // Mostrar Toast
            Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()


            // Ir a LoginActivity y limpiar historial
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}









