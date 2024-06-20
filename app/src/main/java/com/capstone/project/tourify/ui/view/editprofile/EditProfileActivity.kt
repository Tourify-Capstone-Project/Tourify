package com.capstone.project.tourify.ui.view.editprofile

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.capstone.project.tourify.R
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.data.remote.retrofit.ApiConfig
import com.capstone.project.tourify.data.remote.retrofit.ApiService
import com.capstone.project.tourify.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var apiService: ApiService
    private lateinit var token: String

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_IMAGE_PICK = 2
        private const val REQUEST_CAMERA_PERMISSION = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            val userPreference = UserPreference.getInstance(applicationContext)
            val user = userPreference.getSession().first()
            token = user.token
            apiService = ApiConfig.getApiService(token)
        }

        val username = intent.getStringExtra("username")
        binding.edEditProfileUsername.setText(username)

        binding.btnEdit.setOnClickListener {
            val newUsername = binding.edEditProfileUsername.text.toString()
            updateUsername(newUsername)
        }

        binding.btnAddPhoto.setOnClickListener {
            openPhotoOptions()
        }

        setupToolbar()
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

    private fun openPhotoOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Select Option")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
                    } else {
                        dispatchTakePictureIntent()
                    }
                }
                1 -> dispatchPickPictureIntent()
            }
        }
        builder.show()
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun dispatchPickPictureIntent() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK)
    }

    private fun updateUsername(newUsername: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = apiService.updateUsername(newUsername)
                if (response.message == "Username updated successfully!") {
                    val intent = Intent().apply {
                        putExtra("updatedUsername", newUsername)
                    }
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    // Handle error case
                    Log.e("EditProfileActivity", "Failed to update username: ${response.message}")
                }
            } catch (e: Exception) {
                // Handle exception
                e.printStackTrace()
                Log.e("EditProfileActivity", "Exception occurred: ${e.message}")
            }
        }
    }

    private fun uploadPhoto(imageBitmap: Bitmap) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val stream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val byteArray = stream.toByteArray()
                val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), byteArray)
                val photoPart = MultipartBody.Part.createFormData("imgProfile", "profile.jpg", requestBody)
                val response = apiService.uploadPhoto(photoPart)
                if (response.message == "Profile picture updated successfully!") {
                    // Handle success case
                } else {
                    // Handle error case
                    Log.e("EditProfileActivity", "Failed to upload profile photo: ${response.message}")
                }
            } catch (e: Exception) {
                // Handle exception
                e.printStackTrace()
                Log.e("EditProfileActivity", "Exception occurred: ${e.message}")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                dispatchTakePictureIntent()
            } else {
                // Permission denied, show a message to the user
                Log.e("EditProfileActivity", "Camera permission denied")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    binding.imgUser.setImageBitmap(imageBitmap)
                    uploadPhoto(imageBitmap)
                }
                REQUEST_IMAGE_PICK -> {
                    val selectedImageUri: Uri? = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageUri)
                    binding.imgUser.setImageBitmap(bitmap)
                    uploadPhoto(bitmap)
                }
            }
        }
    }
}