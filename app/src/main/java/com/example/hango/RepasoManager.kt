package com.example.hango

import com.example.hango.repaso.Repaso10Activity
import com.example.hango.repaso.Repaso11Activity
import com.example.hango.repaso.Repaso1Activity
import com.example.hango.repaso.Repaso2Activity
import com.example.hango.repaso.Repaso3Activity
import com.example.hango.repaso.Repaso4Activity
import com.example.hango.repaso.Repaso5Activity
import com.example.hango.repaso.Repaso6Activity
import com.example.hango.repaso.Repaso7Activity
import com.example.hango.repaso.Repaso8Activity
import com.example.hango.repaso.Repaso9Activity

object RepasoManager {
    val completadas = mutableSetOf<String>()

    fun registrar(nombreClase: String) {
        completadas.add(nombreClase)
    }

    fun reiniciar() {
        completadas.clear()
    }
    val actividadesRepaso = listOf(
        Repaso1Activity::class.java,
        Repaso2Activity::class.java,
        Repaso3Activity::class.java,
        Repaso4Activity::class.java,
        Repaso5Activity::class.java,
        Repaso6Activity::class.java,
        Repaso7Activity::class.java,
        Repaso8Activity::class.java,
        Repaso9Activity::class.java,
        Repaso10Activity::class.java,
        Repaso11Activity::class.java
    )
}
