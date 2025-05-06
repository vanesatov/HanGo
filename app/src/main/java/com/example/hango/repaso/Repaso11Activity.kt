package com.example.hango.repaso

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.AlfabetoActivity
import com.example.hango.R
import com.example.hango.RepasoManager
import com.example.hango.databinding.ActivityRepaso11Binding
import com.google.android.material.card.MaterialCardView

class Repaso11Activity : AppCompatActivity() {
    private lateinit var binding: ActivityRepaso11Binding
    private var seleccionSonido: MaterialCardView? = null
    private var seleccionLetra: MaterialCardView? = null
    private var aciertos = 0

    private val paresCorrectos = mapOf(
        "ya" to "ㅑ",
        "yu" to "ㅠ",
        "wo" to "ㅝ",
        "wi" to "ㅟ"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRepaso11Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnComprobar.isEnabled = false
        binding.btnComprobar.alpha = 0.5f

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        RepasoManager.registrar(this::class.java.name)

        val indiceActual = intent.getIntExtra("indice", -1) // Índice actual en la secuencia
        val total = intent.getIntExtra("total", 1) // Total de actividades
        val secuencia = intent.getStringArrayExtra("secuencia") // Secuencia de actividades

        if (indiceActual == 0) {
            binding.progressBar.progress = 0
        } else if (indiceActual == total - 1) {
            // Última actividad: solo rellenar hasta 90%
            val progresoParcial = 90
            val animator = ObjectAnimator.ofInt(binding.progressBar, "progress", 0, progresoParcial)
            animator.duration = 400
            animator.interpolator = DecelerateInterpolator()
            animator.start()
        } else {
            // Intermedias
            val progreso = ((indiceActual.toDouble() / (total)) * 100).toInt()
            val animator = ObjectAnimator.ofInt(binding.progressBar, "progress", 0, progreso)
            animator.duration = 400
            animator.interpolator = DecelerateInterpolator()
            animator.start()
        }

        val paresMezclados = paresCorrectos.entries.shuffled().map { it.toPair() }
        val sonidos = paresMezclados.map { it.first }
        val letras = paresMezclados.map { it.second }.shuffled()

        val cardsSonido = listOf(
            binding.cardSonido1,
            binding.cardSonido2,
            binding.cardSonido3,
            binding.cardSonido4
        )
        val botonesAudio = listOf(
            binding.btnReproducirAudio1,
            binding.btnReproducirAudio2,
            binding.btnReproducirAudio3,
            binding.btnReproducirAudio4
        )

        val cardsLetra = listOf(
            binding.cardRoman1,
            binding.cardRoman2,
            binding.cardRoman3,
            binding.cardRoman4
        )

        for (i in sonidos.indices) {
            val sonido = sonidos[i]
            val card = cardsSonido[i]
            val boton = botonesAudio[i]

            card.tag = sonido
            card.setOnClickListener { seleccionarTarjetaSonido(card) }

            boton.setOnClickListener {
                val resId = resources.getIdentifier("letra_$sonido", "raw", packageName)
                if (resId != 0) {
                    MediaPlayer.create(this, resId).apply {
                        start()
                        setOnCompletionListener { it.release() }
                    }
                }
                if (card.isClickable) {
                    seleccionarTarjetaSonido(card)
                }
            }
        }

        for (i in letras.indices) {
            val letra = letras[i]
            val card = cardsLetra[i]
            card.tag = letra
            (card.getChildAt(0) as? TextView)?.text = letra
            card.setOnClickListener { seleccionarTarjetaLetra(card) }
        }

        binding.btnComprobar.setOnClickListener {
            val sonidoSel = seleccionSonido?.tag as? String
            val letraSel = seleccionLetra?.tag as? String

            if (sonidoSel == null || letraSel == null) return@setOnClickListener

            val esCorrecta = paresCorrectos[sonidoSel] == letraSel

            val sonido = if (esCorrecta) R.raw.sonido_correcto else R.raw.sonido_incorrecto
            MediaPlayer.create(this, sonido).apply {
                start()
                setOnCompletionListener { it.release() }
            }

            if (esCorrecta) {
                marcarCorrecta(seleccionSonido!!)
                marcarCorrecta(seleccionLetra!!)
                seleccionSonido!!.isClickable = false
                seleccionLetra!!.isClickable = false
                aciertos++

                if (aciertos == paresCorrectos.size) {
                    binding.includeModalResultado.txtResultado.text = getString(R.string.texto_correcto)
                    binding.includeModalResultado.imgKoala.setImageResource(R.drawable.koala_feliz)
                    binding.includeModalResultado.root.setBackgroundResource(R.drawable.bg_modal_correcto)

                    if (indiceActual == total - 1) {
                        // Llenar el ProgressBar al 100% en la última actividad al comprobar
                        val animator = ObjectAnimator.ofInt(binding.progressBar, "progress", binding.progressBar.progress, 100)
                        animator.duration = 400
                        animator.interpolator = DecelerateInterpolator()
                        animator.start()
                    }

                    binding.includeModalResultado.root.visibility = View.VISIBLE
                    binding.includeModalResultado.imgKoala.startAnimation(
                        AnimationUtils.loadAnimation(this, R.anim.anim_koala_entrada)
                    )
                }
            } else {
                marcarIncorrecta(seleccionSonido!!)
                marcarIncorrecta(seleccionLetra!!)

                val sonidoFallido = seleccionSonido
                val letraFallida = seleccionLetra

                Handler(Looper.getMainLooper()).postDelayed({
                    sonidoFallido?.setBackgroundResource(R.drawable.card_default)
                    letraFallida?.setBackgroundResource(R.drawable.card_default)
                    sonidoFallido?.isClickable = true
                    letraFallida?.isClickable = true
                    seleccionSonido = null
                    seleccionLetra = null
                }, 1000)
            }

            seleccionSonido = null
            seleccionLetra = null
            binding.btnComprobar.isEnabled = false
            binding.btnComprobar.alpha = 0.5f
        }

        binding.includeModalResultado.btnContinuar.setOnClickListener {
            binding.includeModalResultado.modalResultado.visibility = View.GONE

            try {
                if (indiceActual < total - 1) {
                    // Validar que la secuencia no sea null y el índice esté dentro del rango
                    if (secuencia != null && indiceActual + 1 < secuencia.size) {
                        // Obtener la siguiente actividad de la secuencia
                        val siguienteActividad = Class.forName(secuencia[indiceActual + 1])
                        val intent = Intent(this, siguienteActividad)
                        intent.putExtra("indice", indiceActual + 1)
                        intent.putExtra("total", total)
                        intent.putExtra("secuencia", secuencia)
                        startActivity(intent)
                    } else {
                        throw IllegalArgumentException("La secuencia es inválida o el índice está fuera de rango.")
                    }
                } else {
                    // Última actividad, ir a la pantalla final
                    val intent = Intent(this, RepasoFinalActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                // Manejo del error: mostrar un mensaje o realizar una acción específica
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("No se pudo cargar la siguiente actividad.")
                    .setPositiveButton("Aceptar") { _, _ -> }
                    .show()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                // Manejo del error por índice fuera de rango o secuencia inválida
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("La secuencia de actividades es inválida.")
                    .setPositiveButton("Aceptar") { _, _ -> }
                    .show()
            }
        }

        binding.btnCerrar.setOnClickListener { onBackPressed() }
    }

    private fun seleccionarTarjetaSonido(card: MaterialCardView) {
        // Restaurar fondo de la tarjeta anterior si existe
        seleccionSonido?.setBackgroundResource(R.drawable.card_default)

        // Guardar nueva selección
        seleccionSonido = card

        // Aplicar fondo seleccionado
        card.setBackgroundResource(R.drawable.card_selected)

        verificarActivarBoton()
    }

    private fun seleccionarTarjetaLetra(card: MaterialCardView) {
        // Restaurar fondo de la tarjeta anterior si existe
        seleccionLetra?.setBackgroundResource(R.drawable.card_default)

        // Guardar nueva selección
        seleccionLetra = card

        // Aplicar fondo seleccionado
        card.setBackgroundResource(R.drawable.card_selected)

        verificarActivarBoton()
    }

    private fun verificarActivarBoton() {
        val activar = seleccionSonido != null && seleccionLetra != null
        binding.btnComprobar.isEnabled = activar
        binding.btnComprobar.alpha = if (activar) 1f else 0.5f
    }

    private fun marcarCorrecta(card: MaterialCardView) {
        card.setBackgroundResource(R.drawable.card_correct)
    }

    private fun marcarIncorrecta(card: MaterialCardView) {
        card.setBackgroundResource(R.drawable.card_incorrect)
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
