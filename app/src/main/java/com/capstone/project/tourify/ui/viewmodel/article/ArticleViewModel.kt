package com.capstone.project.tourify.ui.viewmodel.article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn

import com.capstone.project.tourify.data.local.entity.category.CategoryEntity
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.launch
import androidx.paging.filter
import com.capstone.project.tourify.data.local.entity.RecommendedItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

class ArticleViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private val _searchResults = MutableStateFlow<PagingData<CategoryEntity>>(PagingData.empty())
    val searchResults: StateFlow<PagingData<CategoryEntity>> = _searchResults

    private val _recommendations = MutableLiveData<List<RecommendedItem>?>()
    val recommendations: LiveData<List<RecommendedItem>?> get() = _recommendations

    // Get headline news
    val getHeadlineNews: LiveData<PagingData<ArticlesResponseItem>> =
        userRepository.getArticles("").cachedIn(viewModelScope)

    // Search destinations based on query
    fun searchDestinations(query: String) {
        _isSearching.value = true
        viewModelScope.launch {
            userRepository.searchDestinations(query)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    val filteredResults = pagingData.filter { it.placeName.contains(query, ignoreCase = true) }
                    _searchResults.value = filteredResults // Update search results
                    _isSearching.value = false
                }
        }
    }

    // Get categories by type
    fun getCategoriesByType(category: String): LiveData<PagingData<CategoryEntity>> {
        return userRepository.getCategoriesByType(category).cachedIn(viewModelScope).asLiveData()
    }

    // Fetch recommendations
    // Ambil rekomendasi
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
