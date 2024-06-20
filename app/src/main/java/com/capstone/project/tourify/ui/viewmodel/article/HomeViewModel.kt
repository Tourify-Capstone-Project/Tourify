package com.capstone.project.tourify.ui.viewmodel.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.project.tourify.data.local.entity.RecommendedItem
import com.capstone.project.tourify.data.local.entity.category.CategoryEntity
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<List<CategoryEntity>>()
    val searchResults: LiveData<List<CategoryEntity>> = _searchResults

    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> = _isSearching

    private val _recommendations = MutableLiveData<List<RecommendedItem>?>()
    val recommendations: LiveData<List<RecommendedItem>?> get() = _recommendations

    fun searchDestinations(query: String) {
        _isSearching.postValue(true)
        viewModelScope.launch {
            val results = userRepository.searchDestinations(query)
            val filteredResults = results.filter { it.placeName.contains(query, ignoreCase = true) }
            _searchResults.postValue(filteredResults)
            _isSearching.postValue(false)
        }
    }

    fun fetchRecommendations() {
        viewModelScope.launch {
            val response = userRepository.getRecommendations()
            response?.let {
                val recommendedItems = it.detailsFavorite.map { item ->
                    RecommendedItem(
                        id = item.recomenId,
                        tourismId = item.tourismId,
                        name = item.detailPlace.placeName,
                        imageUrl = item.detailPlace.placePhotoUrl,
                        price = item.detailPlace.price,
                        rating = item.detailPlace.rating,
                        userId = item.userId
                    )
                }
                _recommendations.postValue(recommendedItems)
            }
        }
    }
}