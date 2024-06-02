package com.capstone.project.tourify.ui.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.ActivityLoginBinding
import com.capstone.project.tourify.ui.customview.CustomEmailInputLayout
import com.capstone.project.tourify.ui.customview.CustomPasswordInputLayout
import com.capstone.project.tourify.ui.view.register.RegisterActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var emailInputLayout: CustomEmailInputLayout
    private lateinit var passwordInputLayout: CustomPasswordInputLayout
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPassword: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notHaveAccountTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        emailInputLayout = findViewById(R.id.emailEditTextLayout)
        passwordInputLayout = findViewById(R.id.passwordEditTextLayout)
        edtEmail = findViewById(R.id.edLoginEmail)
        edtPassword = findViewById(R.id.edLoginPassword)

        emailInputLayout.setEditText(edtEmail)
        passwordInputLayout.setEditText(edtPassword)


        playAnimation()
        setupView()

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -10f, 10f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 0f, 1f).setDuration(500)
        val edLoginEmail =
            ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 0f, 1f).setDuration(500)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 0f, 1f).setDuration(500)
        val edLoginPassword =
            ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 0f, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 0f, 1f)
                .setDuration(500)
        val notHaveAccount =
            ObjectAnimator.ofFloat(binding.notHaveAccountTextView, View.ALPHA, 0f, 1f)
                .setDuration(500)
        val forgotPassword =
            ObjectAnimator.ofFloat(binding.forgotPassword, View.ALPHA, 0F, 1F).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                emailTextView,
                emailEditTextLayout,
                edLoginEmail,
                passwordTextView,
                edLoginPassword,
                passwordEditTextLayout,
                forgotPassword,
                notHaveAccount,
                login
            )
            startDelay = 100
        }.start()
    }
}