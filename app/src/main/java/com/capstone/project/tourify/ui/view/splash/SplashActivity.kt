package com.capstone.project.tourify.ui.view.splash

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import com.capstone.project.tourify.databinding.ActivitySplashBinding
import com.capstone.project.tourify.ui.view.onboardingpage.OnBoardingActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageLogo.cameraDistance = 8000 * resources.displayMetrics.density

        val flipAnimator = ObjectAnimator.ofFloat(binding.imageLogo, "rotationY", 0f, 360f).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
        }

        flipAnimator.start()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
        }, 3000)
    }
}