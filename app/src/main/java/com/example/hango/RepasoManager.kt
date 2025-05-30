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
import com.example.hango.repaso2.RepasoB10Activity
import com.example.hango.repaso2.RepasoB1Activity
import com.example.hango.repaso2.RepasoB2Activity
import com.example.hango.repaso2.RepasoB3Activity
import com.example.hango.repaso2.RepasoB4Activity
import com.example.hango.repaso2.RepasoB5Activity
import com.example.hango.repaso2.RepasoB6Activity
import com.example.hango.repaso2.RepasoB7Activity
import com.example.hango.repaso2.RepasoB8Activity
import com.example.hango.repaso2.RepasoB9Activity
import com.example.hango.repaso3.RepasoC10Activity
import com.example.hango.repaso3.RepasoC1Activity
import com.example.hango.repaso3.RepasoC2Activity
import com.example.hango.repaso3.RepasoC3Activity
import com.example.hango.repaso3.RepasoC4Activity
import com.example.hango.repaso3.RepasoC5Activity
import com.example.hango.repaso3.RepasoC6Activity
import com.example.hango.repaso3.RepasoC7Activity
import com.example.hango.repaso3.RepasoC8Activity
import com.example.hango.repaso3.RepasoC9Activity
import com.example.hango.repaso4.RepasoD10Activity
import com.example.hango.repaso4.RepasoD1Activity
import com.example.hango.repaso4.RepasoD2Activity
import com.example.hango.repaso4.RepasoD3Activity
import com.example.hango.repaso4.RepasoD4Activity
import com.example.hango.repaso4.RepasoD5Activity
import com.example.hango.repaso4.RepasoD6Activity
import com.example.hango.repaso4.RepasoD7Activity
import com.example.hango.repaso4.RepasoD8Activity
import com.example.hango.repaso4.RepasoD9Activity

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

    val actividadesRepaso2 = listOf(
        RepasoB1Activity::class.java,
        RepasoB2Activity::class.java,
        RepasoB3Activity::class.java,
        RepasoB4Activity::class.java,
        RepasoB5Activity::class.java,
        RepasoB6Activity::class.java,
        RepasoB7Activity::class.java,
        RepasoB8Activity::class.java,
        RepasoB9Activity::class.java,
        RepasoB10Activity::class.java
    )

    val actividadesRepaso3 = listOf(
        RepasoC1Activity::class.java,
        RepasoC2Activity::class.java,
        RepasoC3Activity::class.java,
        RepasoC4Activity::class.java,
        RepasoC5Activity::class.java,
        RepasoC6Activity::class.java,
        RepasoC7Activity::class.java,
        RepasoC8Activity::class.java,
        RepasoC9Activity::class.java,
        RepasoC10Activity::class.java
    )

    val actividadesRepaso4 = listOf(
        RepasoD1Activity::class.java,
        RepasoD2Activity::class.java,
        RepasoD3Activity::class.java,
        RepasoD4Activity::class.java,
        RepasoD5Activity::class.java,
        RepasoD6Activity::class.java,
        RepasoD7Activity::class.java,
        RepasoD8Activity::class.java,
        RepasoD9Activity::class.java,
        RepasoD10Activity::class.java
    )
}
