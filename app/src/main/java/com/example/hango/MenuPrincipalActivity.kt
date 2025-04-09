package com.example.hango

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.databinding.ActivityMenuPrincipalBinding

class MenuPrincipalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Aquí enlazamos la tarjeta con el intent a AlfabetoActivity
        binding.cardAlfabeto.setOnClickListener {
            val intent = Intent(this, AlfabetoActivity::class.java)
            startActivity(intent)
        }

        binding.btnUsuario.setOnClickListener {
            startActivity(Intent(this, UsuarioActivity::class.java))
        }
        binding.btnHome.setCardBackgroundColor(ContextCompat.getColor(this, R.color.activo))
        binding.btnUsuario.setCardBackgroundColor(ContextCompat.getColor(this, R.color.inactivo))


        binding.btnUsuario.cardElevation = 10f
        binding.btnHome.cardElevation = 4f
    }
}