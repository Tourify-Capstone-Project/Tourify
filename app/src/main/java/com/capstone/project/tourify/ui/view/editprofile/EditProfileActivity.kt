package com.capstone.project.tourify.ui.view.editprofile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private var imageUri: Uri? = null // variable to store image URI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        storage = Firebase.storage
        storageRef = storage.reference

        setupToolbar()

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val username = user.displayName
            binding.edEditProfileUsername.setText(username)
            user.photoUrl?.let { photoUri ->
                Glide.with(this)
                    .load(photoUri)
                    .into(binding.imgUser)
            }
        }

        binding.btnEdit.setOnClickListener {
            val updatedUsername = binding.edEditProfileUsername.text.toString()
            updateProfileInFirebase(updatedUsername)
        }

        binding.btnAddPhoto.setOnClickListener {
            showImagePicker()
        }
    }

    private fun showImagePicker() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request camera permission if not granted
            requestCameraPermission()
        } else {
            val options = arrayOf<CharSequence>("Ambil Foto", "Pilih dari Galeri")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pilih Aksi")
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> dispatchTakePictureIntent()
                    1 -> dispatchPickPictureIntent()
                }
            }
            builder.show()
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_CODE
        )
    }

    private fun createImageFile(): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir: File? = getExternalFilesDir("Pictures")
            File.createTempFile(
                "JPEG_${timeStamp}_", ".jpg", storageDir
            ).apply {
                imageUri = FileProvider.getUriForFile(
                    this@EditProfileActivity,
                    "${packageName}.fileprovider",
                    this
                )
            }
        } catch (ex: IOException) {
            Log.e(TAG, "Error creating image file: ${ex.message}")
            null
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                createImageFile()?.also { file ->
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "${packageName}.fileprovider",
                        file
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, TAKE_PHOTO)
                }
            }
        }
    }

    private fun dispatchPickPictureIntent() {
        Intent(Intent.ACTION_GET_CONTENT).also { pickPhotoIntent ->
            pickPhotoIntent.type = "image/*"
            startActivityForResult(Intent.createChooser(pickPhotoIntent, "Pilih Gambar"), PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE -> {
                    imageUri = data?.data
                    binding.imgUser.setImageURI(imageUri)
                }
                TAKE_PHOTO -> {
                    binding.imgUser.setImageURI(imageUri)
                }
            }
        }
    }

    private fun updateProfileInFirebase(updatedUsername: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val profileUpdates = userProfileChangeRequest {
                displayName = updatedUsername
            }
            user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                    imageUri?.let { uri ->
                        uploadProfilePicture(uri, user, updatedUsername)
                    } ?: run {
                        val resultIntent = Intent()
                        resultIntent.putExtra("updatedUsername", updatedUsername)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                } else {
                    Log.w(TAG, "Failed to update profile", task.exception)
                }
            }
        }
    }

    private fun uploadProfilePicture(uri: Uri, user: FirebaseUser, updatedUsername: String) {
        val filePath = "user-profile-img/${user.uid}/${uri.lastPathSegment}"
        Log.d(TAG, "Uploading to path: $filePath with UID: ${user.uid}")

        val profilePicRef = storageRef.child(filePath)
        profilePicRef.putFile(uri)
            .addOnSuccessListener {
                profilePicRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val profileUpdatesWithPhoto = userProfileChangeRequest {
                        photoUri = downloadUri
                    }
                    user.updateProfile(profileUpdatesWithPhoto).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User profile photo updated.")
                            val resultIntent = Intent().apply {
                                putExtra("updatedUsername", updatedUsername)
                                putExtra("photoUrl", downloadUri.toString())
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        } else {
                            Log.w(TAG, "Failed to update profile photo", task.exception)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error uploading profile picture", e)
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
        private const val PICK_IMAGE = 1
        private const val TAKE_PHOTO = 2
        private const val CAMERA_PERMISSION_CODE = 101
    }
}