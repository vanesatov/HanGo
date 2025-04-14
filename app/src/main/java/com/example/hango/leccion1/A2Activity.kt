package com.example.hango.leccion1

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.AlfabetoActivity
import com.example.hango.R
import com.example.hango.databinding.ActivityA2Binding

class A2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityA2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityA2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nombreClase = this::class.java.simpleName
        val totalActividades = 15
        calcularYAnimarProgreso(nombreClase, totalActividades)

        binding.btnCerrar.setOnClickListener {
            onBackPressed()
        }

        binding.btnSiguiente.setOnClickListener {
             val intent = Intent(this, A3Activity::class.java)
             startActivity(intent)
        }

        val mediaPlayer = MediaPlayer.create(this, R.raw.letra_i)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            it.release()
        }
    }

    private fun calcularYAnimarProgreso(nombreClase: String, total: Int) {
        val regex = Regex("[A-Z](\\d+).*")
        val match = regex.find(nombreClase)
        val numero = match?.groupValues?.get(1)?.toIntOrNull() ?: 1
        val porcentaje = (numero.toFloat() / total.toFloat() * 100).toInt()
        animarProgreso(porcentaje)
    }

    private fun animarProgreso(progresoFinal: Int) {
        val animator = ObjectAnimator.ofInt(binding.progressBar, "progress", 0, progresoFinal)
        animator.duration = 800
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_salir_leccion, null)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Sí") { _, _ ->
                val intent = Intent(this, AlfabetoActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}