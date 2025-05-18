package com.example.hango.introduccion

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.AlfabetoActivity
import com.example.hango.R
import com.example.hango.databinding.ActivityIntro1Binding

class Intro1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityIntro1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityIntro1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnAnterior.visibility = View.INVISIBLE
        binding.btnAnteriorCard.visibility = View.INVISIBLE

        binding.btnAnteriorCard.strokeColor = getColor(R.color.gris_claro)

        binding.btnSiguiente.setOnClickListener {
            val intent = Intent(this, Intro2Activity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, AlfabetoActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}