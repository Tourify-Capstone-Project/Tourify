package com.capstone.project.tourify.ui.viewmodel.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.project.tourify.data.local.entity.category.CategoryEntity
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<List<CategoryEntity>>()
    val searchResults: LiveData<List<CategoryEntity>> = _searchResults

    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> = _isSearching

    fun searchDestinations(query: String) {
        _isSearching.postValue(true)
        viewModelScope.launch {
            val results = userRepository.searchDestinations(query)
            val filteredResults = results.filter { it.placeName.contains(query, ignoreCase = true) }
            _searchResults.postValue(filteredResults)
            _isSearching.postValue(false)
        }
    }
}