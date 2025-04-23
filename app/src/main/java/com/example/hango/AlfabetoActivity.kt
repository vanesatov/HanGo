package com.example.hango

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hango.databinding.ActivityAlfabetoBinding
import com.example.hango.leccion1.A1Activity
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class AlfabetoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlfabetoBinding
    private val database = FirebaseDatabase.getInstance().reference
    private val uid = FirebaseAuth.getInstance().currentUser?.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAlfabetoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (uid != null) {
            FirebaseDatabase.getInstance().getReference("usuarios")
                .child(uid)
                .child("lecciones")
                .child("alfabeto")
                .child("entrada")
                .setValue(true)
        }

        uid?.let {
            database.child("usuarios").child(it).child("nivel").get()
                .addOnSuccessListener { dataSnapshot ->
                    val nivel = dataSnapshot.getValue(Int::class.java) ?: 0
                    actualizarTarjetas(nivel)
                }
        }

        binding.leccion1.setOnClickListener {
            val intent = Intent(this, A1Activity::class.java)
            startActivity(intent)
        }
    }

    private fun actualizarTarjetas(nivel: Int) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().reference

        database.child("usuarios").child(uid).child("lecciones").get()
            .addOnSuccessListener { snapshot ->
                for (i in 1..8) {
                    val cardId = resources.getIdentifier("leccion$i", "id", packageName)
                    val candadoId = resources.getIdentifier("iconCandadoL$i", "id", packageName)

                    val card = findViewById<MaterialCardView>(cardId)
                    val candado = findViewById<ImageView>(candadoId)

                    val leccion = snapshot.child("leccion$i")
                    val completada =
                        leccion.child("completada").getValue(Boolean::class.java) ?: false
                    val abierta = leccion.child("abierta").getValue(Boolean::class.java) ?: false

                    if (i == 1 || nivel >= i - 1) {
                        card.isEnabled = true
                        card.alpha = 1f

                        when {
                            completada -> {
                                candado.visibility = View.VISIBLE
                                candado.setImageResource(R.drawable.check)
                            }

                            !abierta -> {
                                candado.visibility = View.VISIBLE
                                candado.setImageResource(R.drawable.candado_abierto)
                            }
                            else -> {
                                candado.visibility = View.INVISIBLE
                            }
                        }
                    } else {
                        card.isEnabled = false
                        card.alpha = 0.4f
                        candado.visibility = View.VISIBLE
                        candado.setImageResource(R.drawable.candado_cerrado)
                    }
                }
            }
    }
}
