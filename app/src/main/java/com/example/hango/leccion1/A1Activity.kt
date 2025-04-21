package com.example.hango.leccion1

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.AlfabetoActivity
import com.example.hango.R
import com.example.hango.databinding.ActivityA1Binding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class A1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityA1Binding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityA1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCerrar.setOnClickListener {
            onBackPressed()
        }

        binding.btnSiguiente.setOnClickListener {
            val intent = Intent(this, A2Activity::class.java)
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
        mediaPlayer = MediaPlayer.create(this, R.raw.letra_a)
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
