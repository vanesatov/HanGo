package com.example.hango.repaso3

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.AlfabetoActivity
import com.example.hango.R
import com.example.hango.RepasoManager
import com.example.hango.databinding.ActivityRepasoC10Binding
import com.example.hango.repaso.RepasoFinalActivity
import com.google.android.material.card.MaterialCardView

class RepasoC10Activity : AppCompatActivity() {
    private lateinit var binding: ActivityRepasoC10Binding
    private var opcionSeleccionada: View? = null
    private lateinit var opciones: List<View>
    private var respuestaCorrectaId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRepasoC10Binding.inflate(layoutInflater)
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

        data class Opcion(val texto: String, val solucion: String, val esCorrecta: Boolean)

        val listaOpciones = listOf(
            Opcion("kku", "꾸", false),
            Opcion("su", "수", true),
            Opcion("sso", "쏘", false),
            Opcion("bo", "보", false)
        ).shuffled()

        val tarjetas = listOf(
            Triple(binding.opcion1, binding.txtOpcion1, binding.txtSolucion1),
            Triple(binding.opcion2, binding.txtOpcion2, binding.txtSolucion2),
            Triple(binding.opcion3, binding.txtOpcion3, binding.txtSolucion3),
            Triple(binding.opcion4, binding.txtOpcion4, binding.txtSolucion4)
        )

        for (i in tarjetas.indices) {
            val (card, txt, sol) = tarjetas[i]
            val opcion = listaOpciones[i]

            txt.text = opcion.texto
            sol.text = opcion.solucion
            sol.visibility = View.GONE

            card.tag = opcion.esCorrecta
            if (opcion.esCorrecta) {
                respuestaCorrectaId = card.id
                binding.txtLetra.text = opcion.solucion
            }
            (card.getChildAt(0)).setBackgroundResource(R.drawable.card_default)
        }

        opciones = listOf(binding.opcion1, binding.opcion2, binding.opcion3, binding.opcion4)

        binding.btnComprobar.isEnabled = false
        binding.btnComprobar.alpha = 0.5f

        for (opcion in opciones) {
            opcion.setOnClickListener {
                opcionSeleccionada?.let {
                    val anterior = (it as MaterialCardView).getChildAt(0)
                    anterior.setBackgroundResource(R.drawable.card_default)
                }

                val contenido = (opcion as MaterialCardView).getChildAt(0)
                contenido.setBackgroundResource(R.drawable.card_selected)
                opcionSeleccionada = opcion

                binding.btnComprobar.isEnabled = true
                binding.btnComprobar.alpha = 1f
            }
        }

        binding.btnCerrar.setOnClickListener {
            onBackPressed()
        }

        binding.btnComprobar.setOnClickListener {
            val esCorrecta = opcionSeleccionada?.tag == true
            val sonido = if (esCorrecta) R.raw.sonido_correcto else R.raw.sonido_incorrecto
            val mediaPlayer = MediaPlayer.create(this, sonido)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { it.release() }


            val mostrarSolucion = getSharedPreferences("configuracion", Context.MODE_PRIVATE)
                .getBoolean("mostrar_solucion", true)
            if (mostrarSolucion) {
                tarjetas.forEach { (_, _, sol) -> sol.visibility = View.VISIBLE }
            }

            if (esCorrecta) {
                val correcto = (opcionSeleccionada as MaterialCardView).getChildAt(0)
                correcto.setBackgroundResource(R.drawable.card_correct)

                binding.includeModalResultado.txtResultado.text = getString(R.string.texto_correcto)
                binding.includeModalResultado.imgKoala.setImageResource(R.drawable.koala_feliz)
                binding.includeModalResultado.root.setBackgroundResource(R.drawable.bg_modal_correcto)
            } else {
                val incorrecto = (opcionSeleccionada as MaterialCardView).getChildAt(0)
                incorrecto.setBackgroundResource(R.drawable.card_incorrect)

                val correcta = findViewById<MaterialCardView>(respuestaCorrectaId).getChildAt(0)
                correcta.setBackgroundResource(R.drawable.card_correct)

                binding.includeModalResultado.txtResultado.text = getString(R.string.texto_incorrecto)
                binding.includeModalResultado.imgKoala.setImageResource(R.drawable.koala_sorprendido)
                binding.includeModalResultado.root.setBackgroundResource(R.drawable.bg_modal_incorrecto)
            }

            opciones.forEach { it.isClickable = false }

            if (indiceActual == total - 1) {
                val animator = ObjectAnimator.ofInt(binding.progressBar, "progress", binding.progressBar.progress, 100)
                animator.duration = 400
                animator.interpolator = DecelerateInterpolator()
                animator.start()
            }

            binding.includeModalResultado.root.visibility = View.VISIBLE
            val anim = AnimationUtils.loadAnimation(this, R.anim.anim_koala_entrada)
            binding.includeModalResultado.imgKoala.startAnimation(anim)

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
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("No se pudo cargar la siguiente actividad.")
                    .setPositiveButton("Aceptar") { _, _ -> }
                    .show()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                // Manejo del error por índice fuera de rango o secuencia inválida
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("La secuencia de actividades es inválida.")
                    .setPositiveButton("Aceptar") { _, _ -> }
                    .show()
            }
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
}