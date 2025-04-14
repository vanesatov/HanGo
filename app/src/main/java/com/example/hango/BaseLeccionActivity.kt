package com.example.hango

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

abstract class BaseLeccionActivity : AppCompatActivity() {
    protected lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // No se hace setContentView aquí porque lo hará cada Activity hija
    }

    fun configurarBarraProgreso(valor: Int, maximo: Int) {
        progressBar.progress = valor
        progressBar.max = maximo
    }
}