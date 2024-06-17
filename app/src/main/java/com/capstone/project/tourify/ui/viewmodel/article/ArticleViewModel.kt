package com.capstone.project.tourify.ui.viewmodel.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.capstone.project.tourify.data.local.entity.CategoryEntity
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ArticleViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private val _searchResults = MutableStateFlow<PagingData<CategoryEntity>>(PagingData.empty())
    val searchResults: StateFlow<PagingData<CategoryEntity>> = _searchResults

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
}
