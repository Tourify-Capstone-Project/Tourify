package com.capstone.project.tourify.ui.view.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.ActivitySplashBinding
import com.capstone.project.tourify.ui.view.onboardingpage.OnBoardingActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animFadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        binding.root.startAnimation(animFadeIn)

        animFadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                val animFadeOut = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
                binding.root.startAnimation(animFadeOut)

                animFadeOut.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        binding.root.setBackgroundColor(getColor(R.color.white))
                        hideAllViews()

                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                            finish()
                        }, 100)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                })
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun hideAllViews() {
        // Menyembunyikan semua anak dari root layout
        for (i in 0 until binding.root.childCount) {
            binding.root.getChildAt(i).visibility = View.GONE
        }
    }
}