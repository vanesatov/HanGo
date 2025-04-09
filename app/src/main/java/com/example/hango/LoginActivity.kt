package com.example.hango

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.content.Intent

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        auth = FirebaseAuth.getInstance()
        prefs = getSharedPreferences("HanGoPrefs", MODE_PRIVATE)

        // Verifica si hay email guardado
        cargarEmailGuardado()

        // Si ya hay usuario logueado, ir directamente al menú principal
        if (auth.currentUser != null) {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
            finish()
        }

        binding.btnIniciarSesion.setOnClickListener {
            val email = binding.etEmailLogin.text.toString().trim()
            val password = binding.etPasswordLogin.text.toString().trim()

            if (validarCampos(email, password)) {
                loginUsuario(email, password)
            }
        }

        binding.btnIrARegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    private fun loginUsuario(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                guardarEmailSiEsNecesario()
                Toast.makeText(this, "Sesión iniciada correctamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MenuPrincipalActivity::class.java))
                finish()
            } else {
                val mensajeError = when (task.exception?.message) {
                    "The email address is badly formatted." ->
                        "El formato del correo electrónico no es válido."
                    "There is no user record corresponding to this identifier. The user may have been deleted." ->
                        "No existe una cuenta con ese correo electrónico."
                    "The password is invalid or the user does not have a password." ->
                        "La contraseña es incorrecta."
                    else ->
                        "Error al iniciar sesión. Verifica tus credenciales."
                }

                Toast.makeText(this, mensajeError, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun guardarEmailSiEsNecesario() {
        val editor = prefs.edit()
        if (binding.cbRecordar.isChecked) {
            editor.putString("email", binding.etEmailLogin.text.toString().trim())
            editor.putString("password", binding.etPasswordLogin.text.toString().trim())
            editor.putBoolean("recordar", true)
        } else {
            editor.clear()
        }
        editor.apply()
    }

    private fun cargarEmailGuardado() {
        val recordar = prefs.getBoolean("recordar", false)
        if (recordar) {
            val email = prefs.getString("email", "")
            val password = prefs.getString("password", "") // <-- Añadido

            binding.etEmailLogin.setText(email)
            binding.etPasswordLogin.setText(password) // <-- Añadido
            binding.cbRecordar.isChecked = true
        }
    }


    private fun validarCampos(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, "El email no puede estar vacío", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "El email no tiene un formato válido", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}