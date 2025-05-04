package com.example.hango

import android.animation.ObjectAnimator
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

abstract class BaseLeccionActivity : AppCompatActivity() {
    protected lateinit var progressBar: ProgressBar
    private var progresoAnterior = 0

    fun calcularYAnimarProgreso(nombreClase: String, total: Int) {
        val numero = Regex("\\d+").find(nombreClase)?.value?.toIntOrNull() ?: 1
        val porcentaje = (numero.toFloat() / total.toFloat() * 100).toInt()
        animarProgreso(porcentaje)
    }

    protected fun animarProgreso(progresoFinal: Int) {
        val animator = ObjectAnimator.ofInt(progressBar, "progress", progresoAnterior, progresoFinal)
        animator.duration = 400
        animator.interpolator = DecelerateInterpolator()
        animator.start()
        progresoAnterior = progresoFinal
    }
}