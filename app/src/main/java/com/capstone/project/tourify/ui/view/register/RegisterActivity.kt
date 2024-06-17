package com.capstone.project.tourify.ui.view.register

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.capstone.project.tourify.R
import com.capstone.project.tourify.data.local.entity.UserModel
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.data.remote.retrofit.AuthApiConfig
import com.capstone.project.tourify.data.repository.AuthRepository
import com.capstone.project.tourify.databinding.ActivityRegisterBinding
import com.capstone.project.tourify.ui.customview.CustomEmailInputLayout
import com.capstone.project.tourify.ui.customview.CustomPasswordInputLayout
import com.capstone.project.tourify.ui.view.MainActivity
import com.capstone.project.tourify.ui.view.login.LoginActivity
import com.capstone.project.tourify.ui.viewmodel.register.RegisterViewModel
import com.capstone.project.tourify.ui.viewmodelfactory.AuthViewModelFactory
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

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

    private lateinit var auth: FirebaseAuth

    private lateinit var emailInputLayout: CustomEmailInputLayout
    private lateinit var passwordInputLayout: CustomPasswordInputLayout
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iHaveAccountTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

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

                else -> {}
            }
        }

        auth = Firebase.auth

        binding.signWithGoogle.setOnClickListener {
            signIn()
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

    private fun signIn() {
        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.your_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = this@RegisterActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(LoginActivity.TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(LoginActivity.TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(LoginActivity.TAG, "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(LoginActivity.TAG, "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser
                    user?.let {
                        lifecycleScope.launch {
                            saveUserSession(it)
                        }
                    }
                    updateUI(user)
                } else {
                    Log.w(LoginActivity.TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
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
                val userPreference = UserPreference.getInstance(this@RegisterActivity)
                userPreference.getSession().collect { session ->
                    if (session.isLogin) {
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
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
        private const val TAG = "MainActivity"
        const val PREF_NAME = "UserPreferences"
        const val KEY_USER_TOKEN = "userToken"
    }

}