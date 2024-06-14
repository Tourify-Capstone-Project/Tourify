package com.capstone.project.tourify.ui.view.editprofile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupToolbar()

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val username = user.displayName
            binding.edEditProfileUsername.setText(username)
        }

        binding.btnEdit.setOnClickListener {
            val updatedUsername = binding.edEditProfileUsername.text.toString()

            updateUsernameInFirebase(updatedUsername)
        }
    }

    private fun updateUsernameInFirebase(updatedUsername: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            // Update username in Firebase Authentication
            val profileUpdates = userProfileChangeRequest {
                displayName = updatedUsername
            }

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                        // Kirim kembali hasil ke ProfileFragment
                        val resultIntent = Intent()
                        resultIntent.putExtra("updatedUsername", updatedUsername)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    } else {
                        Log.w(TAG, "Failed to update profile", task.exception)
                        // Handle failure here
                    }
                }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.materialBarEditProfile)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            title = getString(R.string.string_edit_profile)
        }

        binding.materialBarEditProfile.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        private const val TAG = "EditProfileActivity"
    }
}