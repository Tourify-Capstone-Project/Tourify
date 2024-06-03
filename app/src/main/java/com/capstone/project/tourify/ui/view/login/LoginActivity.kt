package com.capstone.project.tourify.ui.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.capstone.project.tourify.R
import com.capstone.project.tourify.data.local.room.UserModel
import com.capstone.project.tourify.databinding.ActivityLoginBinding
import com.capstone.project.tourify.ui.customview.CustomEmailInputLayout
import com.capstone.project.tourify.ui.customview.CustomPasswordInputLayout
import com.capstone.project.tourify.ui.view.MainActivity
import com.capstone.project.tourify.ui.view.register.RegisterActivity
import com.capstone.project.tourify.ui.viewmodel.login.LoginViewModel
import com.capstone.project.tourify.ui.viewmodelfactory.AuthViewModelFactory
import com.capstone.project.tourify.utils.wrapEspressoIdlingResource
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    private lateinit var emailInputLayout: CustomEmailInputLayout
    private lateinit var passwordInputLayout: CustomPasswordInputLayout
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPassword: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailInputLayout = findViewById(R.id.emailEditTextLayout)
        passwordInputLayout = findViewById(R.id.passwordEditTextLayout)
        edtEmail = findViewById(R.id.edLoginEmail)
        edtPassword = findViewById(R.id.edLoginPassword)

        emailInputLayout.setEditText(edtEmail)
        passwordInputLayout.setEditText(edtPassword)

        binding.notHaveAccountTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupAction()


    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            viewModel.login(email, password)

            viewModel.isLoading.observe(this) { isLoading ->
                showLoading(isLoading)
            }

            viewModel.loginResult.observe(this) { response ->
                wrapEspressoIdlingResource {
                    if (!response.error!!) {
                        val token = response.loginResult?.token
                        if (token != null) {
                            val userModel = UserModel(email, token, true)
                            viewModel.saveSession(userModel)

                            AlertDialog.Builder(this).apply {
                                setTitle(getString(R.string.string_yeaay))
                                setMessage(getString(R.string.string_success))
                                setPositiveButton(getString(R.string.string_next)) { _, _ ->
                                    val intent =
                                        Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                    } else {
                        AlertDialog.Builder(this).apply {
                            setTitle(getString(R.string.string_error))
                            setMessage(getString(R.string.login_failed))
                            setPositiveButton(getString(R.string.String_ok)) { _, _ -> }
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}