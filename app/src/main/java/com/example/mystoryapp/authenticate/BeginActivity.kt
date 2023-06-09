package com.example.mystoryapp.authenticate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mystoryapp.databinding.ActivityBeginBinding

class BeginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBeginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBeginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@BeginActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@BeginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        playAnimation()
    }
    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.ivBegin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tvBegin = ObjectAnimator.ofFloat(binding.tvBegin, View.ALPHA, 1f).setDuration(500)
        val tvBegin2 = ObjectAnimator.ofFloat(binding.tvBegin2, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login, register)
        }

        AnimatorSet().apply {
            playSequentially(tvBegin, tvBegin2, together)
            start()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}