package com.capstone.project.tourify.ui.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.ActivityRegisterBinding
import com.capstone.project.tourify.ui.customview.CustomEmailInputLayout
import com.capstone.project.tourify.ui.customview.CustomPasswordInputLayout
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var emailInputLayout: CustomEmailInputLayout
    private lateinit var passwordInputLayout: CustomPasswordInputLayout
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailInputLayout = findViewById(R.id.emailEditTextLayout)
        passwordInputLayout = findViewById(R.id.passwordEditTextLayout)
        edtEmail = findViewById(R.id.edRegisterEmail)
        edtPassword = findViewById(R.id.edRegisterPassword)

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

        val usernameTextView =
            ObjectAnimator.ofFloat(binding.usernameTextView, View.ALPHA, 0F, 1F).setDuration(500)
        val usernameEditTextLayout =
            ObjectAnimator.ofFloat(binding.usernameEditTextLayout, View.ALPHA, 0F, 1F).setDuration(500)
        val edRegisterUsername =
            ObjectAnimator.ofFloat(binding.edRegisterUsername, View.ALPHA, 0F, 1F).setDuration(500)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 0f, 1f).setDuration(500)
        val edRegisterEmail =
            ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 0f, 1f).setDuration(500)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 0f, 1f).setDuration(500)
        val edRegisterPassword =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 0f, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 0f, 1f)
                .setDuration(500)
        val iHaveAccount =
            ObjectAnimator.ofFloat(binding.iHaveAccountTextView, View.ALPHA, 0f, 1f)
                .setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 0f, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                usernameTextView,
                usernameEditTextLayout,
                edRegisterUsername,
                emailTextView,
                emailEditTextLayout,
                edRegisterEmail,
                passwordTextView,
                edRegisterPassword,
                passwordEditTextLayout,
                iHaveAccount,
                register
            )
            startDelay = 100
        }.start()
    }
}