package com.capstone.project.tourify.ui.view.editprofile

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.Manifest
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth

    private var imageUri: Uri? = null // variabel untuk menyimpan URI gambar dari kamera

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

        binding.btnAddPhoto.setOnClickListener {
            showImagePicker()
        }
    }

    private fun showImagePicker() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Izin kamera belum diberikan, minta izin secara dinamis
            requestCameraPermission()
        } else {
            val options = arrayOf<CharSequence>("Ambil Foto", "Pilih dari Galeri")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pilih Aksi")
            builder.setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        // Ambil Foto (Kamera)
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                            takePictureIntent.resolveActivity(packageManager)?.also {
                                val photoFile: File? = try {
                                    createImageFile()
                                } catch (ex: IOException) {
                                    Log.e(TAG, "Error creating image file: ${ex.message}")
                                    null
                                }
                                photoFile?.also {
                                    val photoURI: Uri = FileProvider.getUriForFile(
                                        this,
                                        "${packageName}.fileprovider",
                                        it
                                    )
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                    imageUri = photoURI
                                    startActivityForResult(takePictureIntent, TAKE_PHOTO)
                                }
                            }
                        }
                    }

                    1 -> {
                        // Pilih dari Galeri
                        Intent(Intent.ACTION_GET_CONTENT).also { pickPhotoIntent ->
                            pickPhotoIntent.type = "image/*"
                            startActivityForResult(
                                Intent.createChooser(
                                    pickPhotoIntent,
                                    "Pilih Gambar"
                                ), PICK_IMAGE
                            )
                        }
                    }
                }
            }
            builder.show()
        }
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setTitle("Izin Diperlukan")
                .setMessage("Aplikasi ini memerlukan izin kamera untuk mengambil foto.")
                .setPositiveButton(getString(R.string.izinkan)) { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        CAMERA_PERMISSION_CODE
                    )
                }
                .setNegativeButton(getString(R.string.batal)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setTitle("Izin Diperlukan")
                .setMessage("Aplikasi ini memerlukan izin kamera untuk mengambil foto.")
                .setPositiveButton(getString(R.string.izinkan)) { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        CAMERA_PERMISSION_CODE
                    )
                }
                .setNegativeButton(getString(R.string.batal)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }
    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir("Pictures")
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            imageUri = FileProvider.getUriForFile(
                this@EditProfileActivity,
                "${packageName}.fileprovider",
                this
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE -> {
                    val selectedImageUri = data?.data
                    binding.imgUser.setImageURI(selectedImageUri)
                    imageUri = selectedImageUri
                }

                TAKE_PHOTO -> {
                    imageUri?.let { uri ->
                        binding.imgUser.setImageURI(uri)
                    }
                }
            }
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
                        val resultIntent = Intent()
                        resultIntent.putExtra("updatedUsername", updatedUsername)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    } else {
                        Log.w(TAG, "Failed to update profile", task.exception)
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
        private const val PICK_IMAGE = 1
        private const val TAKE_PHOTO = 2
        private const val CAMERA_PERMISSION_CODE = 101
    }
}