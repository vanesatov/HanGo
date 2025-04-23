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
import com.google.firebase.database.DatabaseReference


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
            val userRef = database.child("usuarios").child(it)
            userRef.child("nivel").get().addOnSuccessListener { dataSnapshot ->
                val nivel = dataSnapshot.getValue(Int::class.java) ?: 0
                actualizarTarjetasPorNivel(userRef, nivel)
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

    private fun actualizarTarjetasPorNivel(userRef: DatabaseReference, nivel: Int) {

        binding.cardAlfabeto.isEnabled = true
        binding.cardAlfabeto.alpha = 1f


        userRef.child("lecciones").child("alfabeto").child("entrada").get()
            .addOnSuccessListener { entradaSnap ->
                val haEntradoAlfabeto = entradaSnap.getValue(Boolean::class.java) == true

                binding.iconCandadoAlfabeto.visibility = View.VISIBLE

                when {
                    nivel >= 8 -> {

                        binding.iconCandadoAlfabeto.setImageResource(R.drawable.check)
                    }
                    !haEntradoAlfabeto -> {
                        binding.iconCandadoAlfabeto.setImageResource(R.drawable.candado_abierto)
                    }
                    else -> {
                        binding.iconCandadoAlfabeto.visibility = View.INVISIBLE
                    }
                }
            }

        userRef.child("lecciones").child("colores").child("entrada").get().addOnSuccessListener { entradaSnapshot ->
            val haEntradoColores = entradaSnapshot.getValue(Boolean::class.java) == true

            if (nivel >= 8) {
                binding.cardColores.isEnabled = true
                binding.cardColores.alpha = 1f
                binding.iconCandadoColores.visibility = View.VISIBLE

                if (nivel >= 9) {
                    binding.iconCandadoColores.setImageResource(R.drawable.check)
                } else if (!haEntradoColores) {
                    binding.iconCandadoColores.setImageResource(R.drawable.candado_abierto)
                } else {
                    binding.iconCandadoColores.visibility = View.INVISIBLE
                }
            } else {
                binding.cardColores.isEnabled = false
                binding.cardColores.alpha = 0.4f
                binding.iconCandadoColores.visibility = View.VISIBLE
                binding.iconCandadoColores.setImageResource(R.drawable.candado_cerrado)
            }
        }

        userRef.child("lecciones").child("frutas").child("entrada").get().addOnSuccessListener { entradaSnapshot ->
            val haEntradoFrutas = entradaSnapshot.getValue(Boolean::class.java) == true

            if (nivel >= 9) {
                binding.cardFrutas.isEnabled = true
                binding.cardFrutas.alpha = 1f
                binding.iconCandadoFrutas.visibility = View.VISIBLE

                if (nivel >= 10) {
                    binding.iconCandadoFrutas.setImageResource(R.drawable.check)
                } else if (!haEntradoFrutas) {
                    binding.iconCandadoFrutas.setImageResource(R.drawable.candado_abierto)
                } else {
                    binding.iconCandadoFrutas.visibility = View.INVISIBLE
                }
            } else {
                binding.cardFrutas.isEnabled = false
                binding.cardFrutas.alpha = 0.4f
                binding.iconCandadoFrutas.visibility = View.VISIBLE
                binding.iconCandadoFrutas.setImageResource(R.drawable.candado_cerrado)
            }
        }
    }
    override fun onResume() {
        super.onResume()

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference

        uid?.let {
            val userRef = database.child("usuarios").child(it)
            userRef.child("nivel").get().addOnSuccessListener { dataSnapshot ->
                val nivel = dataSnapshot.getValue(Int::class.java) ?: 0
                actualizarTarjetasPorNivel(userRef, nivel)
            }
        }
    }
}