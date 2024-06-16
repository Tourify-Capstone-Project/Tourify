package com.capstone.project.tourify.ui.viewmodel.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.project.tourify.data.local.entity.category.CategoryEntity
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val _categories = MutableLiveData<List<CategoryEntity>>()
    val categories: LiveData<List<CategoryEntity>> = _categories

    private val _filteredCategories = MutableLiveData<List<CategoryEntity>>()
    val filteredCategories: LiveData<List<CategoryEntity>> = _filteredCategories

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _categories.value = emptyList()
        _filteredCategories.value = emptyList()
    }

    fun getCategoriesByType(category: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.getCategoriesByType(category).let { liveData ->
                    liveData.observeForever { categoriesList ->
                        _categories.value = categoriesList ?: emptyList()
                        _filteredCategories.value = categoriesList ?: emptyList()
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
