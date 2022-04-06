package com.example.yolharakatiqoidalari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.yolharakatiqoidalari.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        binding.splashText.alpha = 0f
        binding.splashText.animate().setDuration(1000).alpha(1f).withEndAction {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}