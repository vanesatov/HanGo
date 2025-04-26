package com.example.hango.leccion2

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.hango.AlfabetoActivity
import com.example.hango.BaseLeccionActivity
import com.example.hango.R
import com.example.hango.databinding.ActivityB16Binding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class B16Activity : BaseLeccionActivity() {
    private lateinit var binding: ActivityB16Binding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityB16Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        progressBar = binding.progressBar

        calcularYAnimarProgreso(this::class.java.simpleName, 23)

        binding.btnCerrar.setOnClickListener {
            onBackPressed()
        }

        binding.btnSiguiente.setOnClickListener {
            val intent = Intent(this, B17Activity::class.java)
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
        mediaPlayer = MediaPlayer.create(this, R.raw.letra_we)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            it.release()
        }
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

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}