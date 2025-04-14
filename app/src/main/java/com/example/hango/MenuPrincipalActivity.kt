package com.example.hango

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.databinding.ActivityMenuPrincipalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.view.View


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

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference

        uid?.let {
            database.child("usuarios").child(it).child("nivel").get().addOnSuccessListener { dataSnapshot ->
                val nivel = dataSnapshot.getValue(Int::class.java) ?: 0
                actualizarTarjetasPorNivel(nivel)
            }
        }


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
    private fun actualizarTarjetasPorNivel(nivel: Int) {

        binding.cardAlfabeto.isEnabled = true
        binding.cardAlfabeto.alpha = 1f

        //  Colores desbloqueado a partir de nivel 8
        if (nivel >= 8) {
            binding.cardColores.isEnabled = true
            binding.cardColores.alpha = 1f
            binding.iconCandadoColores.setImageResource(R.drawable.candado_abierto)
            binding.iconCandadoColores.visibility = View.VISIBLE
        } else {
            binding.cardColores.isEnabled = false
            binding.cardColores.alpha = 0.4f
            binding.iconCandadoColores.setImageResource(R.drawable.candado_cerrado)
            binding.iconCandadoColores.visibility = View.VISIBLE
        }

        //  Frutas desbloqueado a partir de nivel 9
        if (nivel >= 9) {
            binding.cardFrutas.isEnabled = true
            binding.cardFrutas.alpha = 1f
            binding.iconCandadoFrutas.setImageResource(R.drawable.candado_abierto)
            binding.iconCandadoFrutas.visibility = View.VISIBLE
        } else {
            binding.cardFrutas.isEnabled = false
            binding.cardFrutas.alpha = 0.4f
            binding.iconCandadoFrutas.setImageResource(R.drawable.candado_cerrado)
            binding.iconCandadoFrutas.visibility = View.VISIBLE
        }
    }
}