package com.example.hango.introduccion

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.AlfabetoActivity
import com.example.hango.databinding.ActivityIntro2Binding

class Intro2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityIntro2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityIntro2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnAnterior.setOnClickListener {
            val intent = Intent(this, Intro1Activity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSiguiente.setOnClickListener {
            val intent = Intent(this, Intro3Activity::class.java)
            startActivity(intent)
        }


        binding.btnBack.setOnClickListener {
            val intent = Intent(this, AlfabetoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}