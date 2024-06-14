package com.capstone.project.tourify.ui.view.editprofile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        val username = intent.getStringExtra("username")
        binding.edEditProfileUsername.setText(username)

        binding.btnEdit.setOnClickListener {
            val updatedUsername = binding.edEditProfileUsername.text.toString()

            // Kirim kembali hasil ke ProfileFragment
            val resultIntent = Intent()
            resultIntent.putExtra("updatedUsername", updatedUsername)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
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
}