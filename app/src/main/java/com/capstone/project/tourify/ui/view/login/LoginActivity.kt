package com.capstone.project.tourify.ui.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.capstone.project.tourify.R
import com.capstone.project.tourify.data.local.entity.UserModel
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.databinding.ActivityLoginBinding
import com.capstone.project.tourify.di.AuthInjection
import com.capstone.project.tourify.ui.view.MainActivity
import com.capstone.project.tourify.ui.view.register.RegisterActivity
import com.capstone.project.tourify.ui.viewmodelfactory.AuthViewModelFactory
import com.capstone.project.tourify.ui.viewmodel.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authRepository = AuthInjection.provideAuthRepository(this)
        val viewModelFactory = AuthViewModelFactory(authRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
        auth = FirebaseAuth.getInstance()

        // Menghubungkan custom view dengan elemen di layout
        binding.emailEditTextLayout.setEditText(binding.edLoginEmail)
        binding.passwordEditTextLayout.setEditText(binding.edLoginPassword)

        binding.signWithGoogle.setOnClickListener {
            signIn()
        }

        setupAction()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            if (validateEmailAndPassword(email, password)) {
                viewModel.login(email, password)
            }
        }

        binding.notHaveAccountTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewModel.isLoading.observe(this) {
            // Handle loading state
        }

        viewModel.loginResult.observe(this) { response ->
            if (response.message == "Login successful") {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateEmailAndPassword(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun signIn() {
        // Implementasi sign-in dengan Google
    }

    private suspend fun saveUserSession(user: FirebaseUser) {
        val userPreference = UserPreference.getInstance(this)
        val userModel = UserModel(
            email = user.email ?: "",
            password = "",
            token = user.uid,
            displayName = user.displayName ?: "",
            isLogin = true
        )
        userPreference.saveSession(userModel)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            lifecycleScope.launch {
                val userPreference = UserPreference.getInstance(this@LoginActivity)
                userPreference.getSession().collect { session ->
                    if (session.isLogin) {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    companion object {
        const val TAG = "LoginActivity"
    }

}