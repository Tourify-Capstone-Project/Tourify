package com.capstone.project.tourify.ui.view.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.capstone.project.tourify.R
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.data.remote.retrofit.AuthApiConfig
import com.capstone.project.tourify.data.repository.AuthRepository
import com.capstone.project.tourify.databinding.ActivityRegisterBinding
import com.capstone.project.tourify.ui.customview.CustomEmailInputLayout
import com.capstone.project.tourify.ui.customview.CustomPasswordInputLayout
import com.capstone.project.tourify.ui.view.login.LoginActivity
import com.capstone.project.tourify.ui.viewmodel.register.RegisterViewModel
import com.capstone.project.tourify.ui.viewmodelfactory.AuthViewModelFactory
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val RegisterViewModel: RegisterViewModel by viewModels {
        AuthViewModelFactory(
            AuthRepository(
                AuthApiConfig.getAuthApiService(getUserToken()),
                UserPreference.getInstance(this)
            )
        )
    }

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

        binding.iHaveAccountTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.RegisterButton.setOnClickListener {
            val name = binding.edRegisterUsername.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            RegisterViewModel.register(name, email, password)
        }

        RegisterViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        RegisterViewModel.registrationStatus.observe(this) { status ->
            when (status) {
                is RegisterViewModel.RegistrationStatus.Loading -> {
                }

                is RegisterViewModel.RegistrationStatus.Success -> {
                    showDialog(status.message)
                }

                is RegisterViewModel.RegistrationStatus.Error -> {
                    showDialog(status.message)
                }
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(getString(R.string.string_next)) { _, _ ->
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun getUserToken(): String {
        val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_USER_TOKEN, "") ?: ""
    }

    companion object {
        const val PREF_NAME = "UserPreferences"
        const val KEY_USER_TOKEN = "userToken"
    }

}