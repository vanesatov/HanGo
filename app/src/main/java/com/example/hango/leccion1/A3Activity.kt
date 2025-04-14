package com.example.hango.leccion1

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.AlfabetoActivity
import com.example.hango.R
import com.example.hango.databinding.ActivityA3Binding
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog

class A3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityA3Binding
    private lateinit var opciones: List<View>
    private var opcionSeleccionada: View? = null
    private var respuestaCorrectaId: Int = R.id.opcion2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityA3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nombreClase = this::class.java.simpleName
        val totalActividades = 15
        calcularYAnimarProgreso(nombreClase, totalActividades)

        opciones = listOf(binding.opcion1, binding.opcion2, binding.opcion3, binding.opcion4)

        binding.btnComprobar.isEnabled = false
        binding.btnComprobar.alpha = 0.5f


        for (opcion in opciones) {
            opcion.setOnClickListener {
                opcionSeleccionada?.setBackgroundResource(R.drawable.card_default)
                opcion.setBackgroundResource(R.drawable.card_selected)
                opcionSeleccionada = it

                binding.btnComprobar.isEnabled = true
                binding.btnComprobar.alpha = 1f
            }
        }

        binding.btnCerrar.setOnClickListener {
            onBackPressed()
        }

        binding.btnComprobar.setOnClickListener {
            val esCorrecta = opcionSeleccionada?.id == respuestaCorrectaId


            val sonido = if (esCorrecta) R.raw.sonido_correcto else R.raw.sonido_incorrecto
            val mediaPlayer = MediaPlayer.create(this, sonido)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {
                it.release()
            }

            if (esCorrecta) {
                opcionSeleccionada?.setBackgroundResource(R.drawable.card_correct)
                binding.includeModalResultado.txtResultado.text = getString(R.string.texto_correcto)
                binding.includeModalResultado.imgKoala.setImageResource(R.drawable.koala_feliz)
                binding.includeModalResultado.root.setBackgroundResource(R.drawable.bg_modal_correcto)
            } else {
                opcionSeleccionada?.setBackgroundResource(R.drawable.card_incorrect)
                binding.includeModalResultado.txtResultado.text = getString(R.string.texto_incorrecto)
                binding.includeModalResultado.imgKoala.setImageResource(R.drawable.koala_sorprendido)
                binding.includeModalResultado.root.setBackgroundResource(R.drawable.bg_modal_incorrecto)


                val tarjetaCorrecta = findViewById<View>(respuestaCorrectaId)
                tarjetaCorrecta.setBackgroundResource(R.drawable.card_correct)
            }


            for (opcion in opciones) {
                opcion.isClickable = false
            }


            binding.includeModalResultado.root.visibility = View.VISIBLE


            val anim = AnimationUtils.loadAnimation(this, R.anim.anim_koala_entrada)
            binding.includeModalResultado.imgKoala.startAnimation(anim)

            binding.btnComprobar.isEnabled = false
            binding.btnComprobar.alpha = 0.5f
        }

        binding.includeModalResultado.btnContinuar.setOnClickListener {
            binding.includeModalResultado.modalResultado.visibility = View.GONE

             val intent = Intent(this, A4Activity::class.java)
             startActivity(intent)
             finish()
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
