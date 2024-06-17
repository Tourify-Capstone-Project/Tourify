package com.capstone.project.tourify.ui.viewmodel.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.project.tourify.data.local.entity.category.CategoryEntity
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.launch

class ArticleViewModel(private val articlesRepository: UserRepository) : ViewModel() {

    val getHeadlineNews: LiveData<PagingData<ArticlesResponseItem>> =
        articlesRepository.getArticles().cachedIn(viewModelScope)

    private val _searchResults = MutableLiveData<List<CategoryEntity>>()
    val searchResults: LiveData<List<CategoryEntity>> = _searchResults

    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> = _isSearching

    fun searchDestinations(query: String) {
        _isSearching.postValue(true)
        viewModelScope.launch {
            val results = articlesRepository.searchDestinations(query)
            val filteredResults = results.filter { it.placeName.contains(query, ignoreCase = true) }
            _searchResults.postValue(filteredResults)
            _isSearching.postValue(false)
        }
    }

    fun getCategoriesByType(category: String): LiveData<List<CategoryEntity>> {
        return articlesRepository.getCategoriesByType(category)
    }


}
