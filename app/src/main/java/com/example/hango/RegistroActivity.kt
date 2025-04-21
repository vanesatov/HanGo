package com.example.hango

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        binding.btnRegistrarse.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val passwordRepeat = binding.etPasswordRepeat.text.toString().trim()

            if (validarCampos(email, password, passwordRepeat)) {
                registrarUsuario(email, password)
            }
        }
    }

    private fun registrarUsuario(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val usuario = FirebaseAuth.getInstance().currentUser
                val uid = usuario?.uid

                val database = FirebaseDatabase.getInstance().getReference("usuarios")
                val datosUsuario = hashMapOf(
                    "email" to usuario?.email,
                    "nivel" to 0
                )
                uid?.let {
                    database.child(it).setValue(datosUsuario)
                }

                Toast.makeText(
                    this,
                    "¡Registro exitoso! Inicia sesión para continuar.",
                    Toast.LENGTH_SHORT
                ).show()
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validarCampos(email: String, password: String, passwordRepeat: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, "El email no puede estar vacío", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "El email no tiene un formato válido", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != passwordRepeat) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}