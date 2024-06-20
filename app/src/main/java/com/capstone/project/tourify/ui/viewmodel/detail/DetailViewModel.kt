package com.capstone.project.tourify.ui.viewmodel.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.project.tourify.data.local.entity.favorite.FavoriteEntity
import com.capstone.project.tourify.data.remote.response.DetailResponse
import com.capstone.project.tourify.data.remote.response.ReviewResponse
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    private val _detail = MutableLiveData<DetailResponse?>()
    val detail: LiveData<DetailResponse?> get() = _detail

    private val _reviews = MutableLiveData<ReviewResponse>()
    val reviews: LiveData<ReviewResponse>
        get() = _reviews

    fun getDetail(tourismId: String) {
        viewModelScope.launch {
            _detail.value = repository.getDetail(tourismId)
        }
    }

    suspend fun addFavorite(favorite: FavoriteEntity) {
        repository.addFavorite(favorite)
    }

    suspend fun removeFavoriteById(id: String) {
        repository.removeFavoriteById(id)
    }

    suspend fun addFavoriteToRemote(tourismId: String): Response<DetailResponse> {
        return repository.addFavoriteToRemote(tourismId)
    }

    suspend fun removeFavoriteFromRemote(tourismId: String): Response<DetailResponse> {
        return repository.removeFavoriteFromRemote(tourismId)
    }


    suspend fun getFavoriteById(id: String): FavoriteEntity? {
        return repository.getFavoriteById(id)
    }

    fun fetchReviews(tourismId: String) {
        viewModelScope.launch {
            try {
                val reviewResponse = repository.getReviews(tourismId)
                _reviews.value = reviewResponse
            } catch (e: Exception) {
                // Handle error case
            }
        }
    }

    fun submitReview(tourismId: String, review: String) {
        viewModelScope.launch {
            repository.submitReview(tourismId, review)
            fetchReviews(tourismId)
        }
    }
}
