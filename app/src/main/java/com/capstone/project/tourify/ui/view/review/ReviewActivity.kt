package com.capstone.project.tourify.ui.view.review

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.capstone.project.tourify.R
import com.capstone.project.tourify.data.local.entity.UserModel
import com.capstone.project.tourify.data.remote.response.ReviewUserResponse
import com.capstone.project.tourify.data.remote.retrofit.ApiConfig
import com.capstone.project.tourify.di.Injection
import com.capstone.project.tourify.databinding.ActivityReviewBinding
import com.capstone.project.tourify.ui.viewmodel.detail.DetailViewModel
import com.capstone.project.tourify.ui.viewmodelfactory.ViewModelFactory
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding
    private lateinit var token: String
    private lateinit var viewModel: DetailViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private var tourismId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        tourismId = intent.getStringExtra("tourism_id")

        if (tourismId == null) {
            showToast("Tourism ID not found")
            finish()
            return
        }

        val userRepository = Injection.provideUserRepository(this)
        viewModelFactory = ViewModelFactory(userRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        viewModel.getSession().observe(this, Observer { userModel: UserModel? ->
            if (userModel != null) {
                token = userModel.token
                setupClickListeners()
            } else {
                showToast("Failed to get token")
                finish()
            }
        })
    }

    private fun setupClickListeners() {
        binding.uploadButton.setOnClickListener {
            val description = binding.edAddDescription.text.toString()
            tourismId?.let {
                uploadStory(token, it, description)
            }
        }
    }

    private fun uploadStory(token: String, tourismId: String, reviewDesc: String) {
        showLoading(true)

        lifecycleScope.launch {
            try {
                val apiService = ApiConfig.getApiService(token)
                val response = apiService.submitReview(tourismId, reviewDesc)
                showToast(response.message ?: "Upload success")
                showLoading(false)
                navigateToReviewFragment(tourismId) // Navigasi kembali ke ReviewFragment setelah berhasil
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                if (errorBody != null) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, ReviewUserResponse::class.java)
                        showToast(errorResponse.message ?: "Upload failed: Please log in first")
                    } catch (jsonException: JsonSyntaxException) {
                        showToast("Upload failed: $errorBody")
                    }
                } else {
                    showToast("Upload failed with unknown error")
                }
                showLoading(false)
            } catch (e: Exception) {
                showToast("Upload failed: ${e.message}")
                showLoading(false)
            }
        }
    }

    private fun navigateToReviewFragment(tourismId: String) {
        val intent = Intent().apply {
            putExtra("tourism_id", tourismId)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.materialBarCategory)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            title = getString(R.string.review)
        }

        binding.materialBarCategory.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}

