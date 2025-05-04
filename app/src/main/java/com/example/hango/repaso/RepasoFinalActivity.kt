package com.example.hango.repaso

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hango.AlfabetoActivity
import com.example.hango.R
import com.example.hango.RepasoManager
import com.example.hango.databinding.ActivityRepasoFinalBinding

class RepasoFinalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepasoFinalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRepasoFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val koalas = listOf(
            R.drawable.koala_feliz,
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

        val giroSuave =
            ObjectAnimator.ofFloat(binding.imgKoala, "rotation", 0f, 10f, -10f, 5f, -5f, 0f).apply {
                duration = 1200
            }

        val set = AnimatorSet()
        set.playTogether(moverAlCentro, giroSuave)
        set.start()

        binding.btnTerminar.setOnClickListener {
            RepasoManager.reiniciar()
            val intent = Intent(this, AlfabetoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
        @Suppress("MissingSuperCall")
        override fun onBackPressed() {
            Toast.makeText(this, "Pulsa Terminar para continuar", Toast.LENGTH_SHORT).show()
        }
}