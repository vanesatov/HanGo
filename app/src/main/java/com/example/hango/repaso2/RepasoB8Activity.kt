package com.example.hango.repaso2

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
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
import com.example.hango.databinding.ActivityRepasoB8Binding
import com.example.hango.repaso.RepasoFinalActivity
import com.google.android.material.card.MaterialCardView

class RepasoB8Activity : AppCompatActivity() {
    private lateinit var binding: ActivityRepasoB8Binding
    private var seleccionHangul: MaterialCardView? = null
    private var seleccionRoman: MaterialCardView? = null
    private var aciertos = 0

    private val paresCorrectos = mapOf(
        "개" to "gae",
        "드" to "deu",
        "떠" to "tteo",
        "타" to "ta"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRepasoB8Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnComprobar.isEnabled = false
        binding.btnComprobar.alpha = 0.5f

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


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

        RepasoManager.registrar(this::class.java.name)

        // Mezclar por separado
        val hangulList = paresCorrectos.keys.shuffled()
        val romanList = paresCorrectos.values.shuffled()

        val hangulCards = listOf(binding.cardHangul1, binding.cardHangul2, binding.cardHangul3, binding.cardHangul4)
        val romanCards = listOf(binding.cardRoman1, binding.cardRoman2, binding.cardRoman3, binding.cardRoman4)

        val hangulTextIds = listOf(R.id.text1, R.id.text2, R.id.text3, R.id.text4)
        val romanTextIds = listOf(R.id.txtRoman1, R.id.txtRoman2, R.id.txtRoman3, R.id.txtRoman4)
        val solucionTextIds = listOf(R.id.txtSolucionRoman1, R.id.txtSolucionRoman2, R.id.txtSolucionRoman3, R.id.txtSolucionRoman4)

        // Rellenar columna Hangul
        for (i in hangulCards.indices) {
            val tvHangul = findViewById<TextView>(hangulTextIds[i])
            tvHangul.text = hangulList[i]
            hangulCards[i].setOnClickListener { seleccionarTarjeta(hangulCards[i]) }
        }

        // Rellenar columna Romanización con solución oculta
        for (i in romanCards.indices) {
            val tvRoman = romanCards[i].findViewById<TextView>(romanTextIds[i])
            val tvSolucion = romanCards[i].findViewById<TextView>(solucionTextIds[i])

            val roman = romanList[i]
            tvRoman.text = roman

            // Buscar qué Hangul corresponde a esta romanización
            val hangulCorrecto = paresCorrectos.entries.find { it.value == roman }?.key ?: ""
            tvSolucion.text = hangulCorrecto

            romanCards[i].setOnClickListener { seleccionarTarjeta(romanCards[i]) }
        }

        binding.btnCerrar.setOnClickListener { onBackPressed() }

        binding.btnComprobar.setOnClickListener {
            val letraHangul = obtenerTexto(seleccionHangul)
            val letraRoman = obtenerTexto(seleccionRoman)
            val esCorrecta = paresCorrectos[letraHangul] == letraRoman

            val sonido = if (esCorrecta) R.raw.sonido_correcto else R.raw.sonido_incorrecto
            MediaPlayer.create(this, sonido).apply {
                start()
                setOnCompletionListener { it.release() }
            }

            if (esCorrecta) {
                marcarCorrecta(seleccionHangul!!)
                marcarCorrecta(seleccionRoman!!)
                seleccionHangul!!.isClickable = false
                seleccionRoman!!.isClickable = false
                aciertos++

                if (aciertos == paresCorrectos.size) {
                    RepasoManager.registrar(this::class.java.name)

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
                marcarIncorrecta(seleccionHangul!!)
                marcarIncorrecta(seleccionRoman!!)

                // Restaurar el estado después de 1 segundo (1000 ms)
                val hangul = seleccionHangul!!
                val roman = seleccionRoman!!
                hangul.postDelayed({
                    hangul.getChildAt(0).setBackgroundResource(R.drawable.card_default)
                    roman.getChildAt(0).setBackgroundResource(R.drawable.card_default)
                }, 1000)
            }

            seleccionHangul = null
            seleccionRoman = null
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
    }

    private fun obtenerTexto(card: MaterialCardView?): String {
        val child = card?.getChildAt(0)

        return when (child) {
            is TextView -> child.text.toString().trim()
            is ViewGroup -> {
                // Buscar el primer TextView dentro del layout
                for (i in 0 until child.childCount) {
                    val subview = child.getChildAt(i)
                    if (subview is TextView) {
                        return subview.text.toString().trim()
                    }
                }
                ""
            }
            else -> ""
        }
    }

    private fun seleccionarTarjeta(card: MaterialCardView) {
        val texto = obtenerTexto(card)
        val esHangul = paresCorrectos.containsKey(texto)
        val esRoman = paresCorrectos.containsValue(texto)

        if (esHangul) {
            seleccionHangul?.getChildAt(0)?.setBackgroundResource(R.drawable.card_default)
            seleccionHangul = card
        } else if (esRoman) {
            seleccionRoman?.getChildAt(0)?.setBackgroundResource(R.drawable.card_default)
            seleccionRoman = card
        }

        card.getChildAt(0).setBackgroundResource(R.drawable.card_selected)

        val activar = seleccionHangul != null && seleccionRoman != null
        binding.btnComprobar.isEnabled = activar
        binding.btnComprobar.alpha = if (activar) 1f else 0.5f
    }


    private fun marcarCorrecta(card: MaterialCardView) {
        card.getChildAt(0).setBackgroundResource(R.drawable.card_correct)

        // Mostrar solución solo si está activado el switch
        val prefs = getSharedPreferences("configuracion", Context.MODE_PRIVATE)
        val mostrarSolucion = prefs.getBoolean("mostrar_solucion", true)

        if (mostrarSolucion) {
            // Buscar cualquier TextView con id que empiece por "txtSolucionRoman"
            for (i in 0 until card.childCount) {
                val child = card.getChildAt(i)
                if (child is TextView && child.resources.getResourceEntryName(child.id).startsWith("txtSolucionRoman")) {
                    child.visibility = View.VISIBLE
                    break
                } else if (child is androidx.constraintlayout.widget.ConstraintLayout) {
                    // Si está dentro de ConstraintLayout
                    for (j in 0 until child.childCount) {
                        val subChild = child.getChildAt(j)
                        if (subChild is TextView && subChild.resources.getResourceEntryName(subChild.id).startsWith("txtSolucionRoman")) {
                            subChild.visibility = View.VISIBLE
                            break
                        }
                    }
                }
            }
        }
    }

    private fun marcarIncorrecta(card: MaterialCardView) {
        card.getChildAt(0).setBackgroundResource(R.drawable.card_incorrect)
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
