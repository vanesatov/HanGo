package com.example.hango.frutas

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hango.MenuPrincipalActivity
import com.example.hango.R
import com.example.hango.databinding.ActivityFrutasFinalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FrutasFinalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrutasFinalBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFrutasFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val koalas = listOf(
            R.drawable.koala_final_1,
            R.drawable.koala_final_2,
            R.drawable.koala_final_3,
            R.drawable.koala_final_4,
            R.drawable.koala_final_5,
        )
        val imagenAleatoria = koalas.random()

        Glide.with(this)
            .load(imagenAleatoria)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.imgKoala)

        binding.imgKoala.translationX = -1000f

        val moverAlCentro = ObjectAnimator.ofFloat(binding.imgKoala, "translationX", 0f).apply {
            duration = 1200
            interpolator = DecelerateInterpolator()
        }

        val giroSuave = ObjectAnimator.ofFloat(binding.imgKoala, "rotation", 0f, 10f, -10f, 5f, -5f, 0f).apply {
            duration = 1200
        }

        val set = AnimatorSet()
        set.playTogether(moverAlCentro, giroSuave)
        set.start()

        prefs = getSharedPreferences("ErroresHanGo", MODE_PRIVATE)

        prefs.edit().apply {
            prefs.all.keys.filter { it.endsWith("_fallada") }
                .forEach { remove(it) }
            apply()
        }

        binding.btnTerminar.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val database = FirebaseDatabase.getInstance()
            val userRef = database.getReference("usuarios").child(userId)

            userRef.child("nivel").get().addOnSuccessListener { dataSnapshot ->
                val nivelFirebase = dataSnapshot.getValue(Int::class.java) ?: 0

                prefs.edit().putInt("nivel", nivelFirebase).apply()

                if (nivelFirebase < 10) {
                    userRef.child("nivel").setValue(10)
                    prefs.edit().putInt("nivel", 10).apply()
                    binding.txtSubidaNivel.visibility = View.VISIBLE
                } else {
                    binding.txtSubidaNivel.visibility = View.GONE
                }
                userRef.child("lecciones").child("frutas").child("completada").setValue(true)
            }
        }
    }
    @Suppress("MissingSuperCall")
    override fun onBackPressed() {
        Toast.makeText(this, "Pulsa Terminar para continuar", Toast.LENGTH_SHORT).show()
    }
}