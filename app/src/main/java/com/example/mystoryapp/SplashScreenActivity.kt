package com.example.mystoryapp

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.mystoryapp.authenticate.BeginActivity
import com.example.mystoryapp.databinding.ActivitySplashScreenBinding
import com.example.mystoryapp.preferences.UserPreference

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        playAnimation()
        val pref = UserPreference(this)
        val splashTime: Long = 3000
        Handler(Looper.getMainLooper()).postDelayed({
            if (pref.getUSer().isLogin){
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this@SplashScreenActivity, BeginActivity::class.java)
                startActivity(intent)
            }
        }, splashTime)
    }
    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageView, "alpha",0f, 1f).apply {
            duration = 2000
        }.start()
    }
}