package com.example.hango.colores

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.hango.MenuPrincipalActivity
import com.example.hango.R
import com.example.hango.databinding.ActivityColores1Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Colores1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityColores1Binding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityColores1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val prefs = getSharedPreferences("ErroresHanGo", MODE_PRIVATE)
        prefs.edit().apply {
            prefs.all.keys.filter { it.endsWith("_fallada") }
                .forEach { remove(it) }
            apply()
        }

        marcarLeccionComoAbiertaSiEsPrimeraVez()

        binding.btnCerrar.setOnClickListener {
            onBackPressed()
        }

        binding.btnSiguiente.setOnClickListener {
            val intent = Intent(this, Colores2Activity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnAudio.setOnClickListener {
            reproducirSonido()
        }

        lifecycleScope.launch {
            delay(200)
            reproducirSonido()
        }
    }

    private fun reproducirSonido() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, R.raw.rojo)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            it.release()
        }
    }

    private fun marcarLeccionComoAbiertaSiEsPrimeraVez() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val dbRef = FirebaseDatabase.getInstance().reference
            .child("usuarios").child(uid).child("lecciones").child("colores")

        dbRef.child("abierta").get().addOnSuccessListener { snapshot ->
            val yaAbierta = snapshot.getValue(Boolean::class.java) ?: false
            if (!yaAbierta) {
                dbRef.child("abierta").setValue(true)
            }
        }
        dbRef.child("entrada").setValue(true)
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_salir_leccion, null)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Sí") { _, _ ->
                val intent = Intent(this, MenuPrincipalActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
