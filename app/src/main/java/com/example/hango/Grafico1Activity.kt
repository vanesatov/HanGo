package com.example.hango

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.databinding.ActivityGrafico1Binding

class Grafico1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityGrafico1Binding
    private lateinit var tab1: TextView
    private lateinit var tab2: TextView
    private lateinit var imagen1: ImageView
    private lateinit var imagen2: ImageView
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGrafico1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Referencias
        tab1 = findViewById(R.id.tab1)
        tab2 = findViewById(R.id.tab2)
        imagen1 = findViewById(R.id.grafico1_parte1)
        imagen2 = findViewById(R.id.grafico1_parte2)
        btnBack = findViewById(R.id.btnBack)


        // Inicialmente mostramos el primer gráfico
        mostrarGrafico1()

        // Listener del primer botón
        tab1.setOnClickListener {
            mostrarGrafico1()
        }

        // Listener del segundo botón
        tab2.setOnClickListener {
            mostrarGrafico2()
        }
        btnBack.setOnClickListener {
            val intent = Intent(this, AlfabetoActivity::class.java)
            startActivity(intent)
            finish() // Opcional, para que no vuelva a esta pantalla al darle atrás
        }
    }
    private fun mostrarGrafico1() {
        tab1.setBackgroundResource(R.drawable.tab_selected_background)
        tab2.setBackgroundColor(Color.parseColor("#EFEFEF"))

        tab1.setTextColor(Color.parseColor("#462289"))
        tab2.setTextColor(Color.parseColor("#494949"))

        imagen1.setImageResource(R.drawable.h1) // Tu imagen superior
        imagen2.setImageResource(R.drawable.h2) // Tu imagen inferior
    }

    private fun mostrarGrafico2() {
        tab2.setBackgroundResource(R.drawable.tab_selected_background)
        tab1.setBackgroundColor(Color.parseColor("#EFEFEF"))

        tab2.setTextColor(Color.parseColor("#462289"))
        tab1.setTextColor(Color.parseColor("#494949"))

        imagen1.setImageResource(R.drawable.h3) // Imagen superior del segundo gráfico
        imagen2.setImageResource(R.drawable.h4) // Imagen inferior del segundo gráfico
    }
}
