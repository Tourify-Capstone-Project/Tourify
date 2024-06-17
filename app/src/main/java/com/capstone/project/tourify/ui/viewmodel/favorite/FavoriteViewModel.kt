package com.capstone.project.tourify.ui.viewmodel.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.project.tourify.data.local.entity.favorite.FavoriteEntity
import com.capstone.project.tourify.data.remote.response.FavoriteResponse
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: UserRepository) : ViewModel() {

    private val _favorites = MutableLiveData<FavoriteResponse?>()
    val favorites: LiveData<FavoriteResponse?> get() = _favorites

    fun loadFavorites() {
        viewModelScope.launch {
            val response = repository.getFavoriteDestinations()
            _favorites.postValue(response)
        }
    }

    fun getFavoriteById(tourismId: String): LiveData<FavoriteEntity?> {
        val favorite = MutableLiveData<FavoriteEntity?>()
        viewModelScope.launch {
            favorite.postValue(repository.getFavoriteById(tourismId))
        }
        return favorite
    }
}
