package com.example.hango.colores

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.BaseLeccionActivity
import com.example.hango.MenuPrincipalActivity
import com.example.hango.R
import com.example.hango.databinding.ActivityColores12Binding

class Colores12Activity : BaseLeccionActivity() {
    private lateinit var binding: ActivityColores12Binding
    private lateinit var prefs: SharedPreferences
    private var opcionSeleccionada: View? = null
    private lateinit var opciones: List<View>
    private var mediaPlayer: MediaPlayer? = null
    private var respuestaCorrectaId: Int = R.id.opcion1
    private var enModoRepaso = false
    private var respuestaYaComprobada = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityColores12Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        progressBar = binding.progressBar

        enModoRepaso = intent.getBooleanExtra("modo_repaso", false)
        if (!enModoRepaso) {
            calcularYAnimarProgreso(this::class.java.simpleName, 19)
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.txtRevision.visibility = View.VISIBLE
        }

        prefs = getSharedPreferences("ErroresHanGo", MODE_PRIVATE)

        opciones = listOf(binding.opcion1, binding.opcion2, binding.opcion3, binding.opcion4)


        binding.audioIcon1.setOnClickListener {
            reproducirSonido(R.raw.negro)
            if (!respuestaYaComprobada) binding.opcion1.performClick()
        }
        binding.audioIcon2.setOnClickListener {
            reproducirSonido(R.raw.verde)
            if (!respuestaYaComprobada) binding.opcion2.performClick()
        }
        binding.audioIcon3.setOnClickListener {
            reproducirSonido(R.raw.blanco)
            if (!respuestaYaComprobada) binding.opcion3.performClick()
        }
        binding.audioIcon4.setOnClickListener {
            reproducirSonido(R.raw.naranja)
            if (!respuestaYaComprobada) binding.opcion4.performClick()
        }

        for (opcion in opciones) {
            opcion.setOnClickListener {
                if (respuestaYaComprobada) return@setOnClickListener
                opcionSeleccionada?.setBackgroundResource(R.drawable.card_default)
                opcion.setBackgroundResource(R.drawable.card_selected)
                opcionSeleccionada = it

                binding.btnComprobar.isEnabled = true
                binding.btnComprobar.alpha = 1f
            }
        }

        binding.btnComprobar.isEnabled = false
        binding.btnComprobar.alpha = 0.5f

        binding.btnComprobar.setOnClickListener {
            val esCorrecta = opcionSeleccionada?.id == respuestaCorrectaId
            val sonido = if (esCorrecta) R.raw.sonido_correcto else R.raw.sonido_incorrecto
            reproducirSonido(sonido)

            binding.txtRomanizacion1.visibility = View.VISIBLE
            binding.txtRomanizacion2.visibility = View.VISIBLE
            binding.txtRomanizacion3.visibility = View.VISIBLE
            binding.txtRomanizacion4.visibility = View.VISIBLE

            val nombreClase = this::class.java.simpleName

            if (esCorrecta) {
                opcionSeleccionada?.setBackgroundResource(R.drawable.card_correct)
                binding.includeModalResultado.txtResultado.text = getString(R.string.texto_correcto)
                binding.includeModalResultado.imgKoala.setImageResource(R.drawable.koala_feliz)
                binding.includeModalResultado.root.setBackgroundResource(R.drawable.bg_modal_correcto)

                if (enModoRepaso) {
                    prefs.edit().remove("${nombreClase}_fallada").apply()
                }
            } else {
                opcionSeleccionada?.setBackgroundResource(R.drawable.card_incorrect)
                binding.includeModalResultado.txtResultado.text = getString(R.string.texto_incorrecto)
                binding.includeModalResultado.imgKoala.setImageResource(R.drawable.koala_sorprendido)
                binding.includeModalResultado.root.setBackgroundResource(R.drawable.bg_modal_incorrecto)
                findViewById<View>(respuestaCorrectaId).setBackgroundResource(R.drawable.card_correct)

                if (enModoRepaso) {
                    prefs.edit().remove("${nombreClase}_fallada").apply()
                } else {
                    prefs.edit().putBoolean("${nombreClase}_fallada", true).apply()
                }
            }
            respuestaYaComprobada = true

            for (opcion in opciones) {
                opcion.isClickable = false
            }

            binding.includeModalResultado.root.visibility = View.VISIBLE
            val anim = AnimationUtils.loadAnimation(this, R.anim.anim_koala_entrada)
            binding.includeModalResultado.imgKoala.startAnimation(anim)

            binding.btnComprobar.isEnabled = false
            binding.btnComprobar.alpha = 0.5f
        }

        binding.btnCerrar.setOnClickListener {
            onBackPressed()
        }

        binding.includeModalResultado.btnContinuar.setOnClickListener {
            binding.includeModalResultado.modalResultado.visibility = View.GONE

            if (enModoRepaso) {
                finish()
            } else {
                val intent = Intent(this, Colores13Activity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun reproducirSonido(sonido: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, sonido)
        mediaPlayer?.start()
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
        mediaPlayer = null
    }
}