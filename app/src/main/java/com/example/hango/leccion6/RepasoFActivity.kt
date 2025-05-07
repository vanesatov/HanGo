package com.example.hango.leccion6

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hango.leccion5.EFinalActivity

class RepasoFActivity : AppCompatActivity() {
    private lateinit var erroresOrdenados: List<String>
    private var indiceActual = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val errores = intent.getStringArrayExtra("errores")?.toList() ?: emptyList()

        erroresOrdenados = errores.sortedBy {
            Regex("\\d+").find(it)?.value?.toIntOrNull() ?: Int.MAX_VALUE
        }

        if (erroresOrdenados.isNotEmpty()) {
            lanzarSiguienteError()
        } else {
            val intent = Intent(this, FFinalActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onRestart() {
        super.onRestart()

        val prefs = getSharedPreferences("ErroresHanGo", MODE_PRIVATE)

        erroresOrdenados = prefs.all
            .filter { (clave, _) ->
                clave.endsWith("_fallada") && prefs.getBoolean(clave, false)
            }
            .map { (clave, _) ->
                clave.removeSuffix("_fallada")
            }
            .sortedBy { clave ->
                Regex("\\d+").find(clave)?.value?.toIntOrNull() ?: Int.MAX_VALUE
            }

        if (erroresOrdenados.isNotEmpty()) {
            indiceActual = 0
            lanzarSiguienteError()
        } else {
            val intent = Intent(this, FFinalActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun lanzarSiguienteError() {
        if (indiceActual < erroresOrdenados.size) {
            val nombreClase = erroresOrdenados[indiceActual]
            indiceActual++

            try {
                val clase = Class.forName("com.example.hango.leccion6.$nombreClase")
                val intent = Intent(this, clase)
                intent.putExtra("modo_repaso", true)
                startActivity(intent)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                lanzarSiguienteError()
            }
        } else {
            val intent = Intent(this, FFinalActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}