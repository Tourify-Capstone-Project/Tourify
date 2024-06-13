package com.capstone.project.tourify.ui.viewmodel.category.culinary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.project.tourify.data.local.entity.CategoryEntity
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.launch

class CulinaryViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _categories = MutableLiveData<List<CategoryEntity>>()
    val categories: LiveData<List<CategoryEntity>> = _categories

    private val _filteredCategories = MutableLiveData<List<CategoryEntity>>()
    val filteredCategories: LiveData<List<CategoryEntity>> = _filteredCategories

    init {
        _categories.value = emptyList()
        _filteredCategories.value = emptyList()
    }

    fun getCategoriesByType(category: String) {
        viewModelScope.launch {
            userRepository.refreshCategories(category)
            userRepository.getCategoriesByType(category).observeForever { categoryList ->
                _categories.value = categoryList
                _filteredCategories.value = categoryList
            }
        }
    }

    fun filterCategories(query: String) {
        val filteredList = _categories.value?.filter {
            it.placeName.contains(query, ignoreCase = true) || it.placeId.contains(query, ignoreCase = true)
        } ?: emptyList()
        _filteredCategories.value = filteredList
    }
}
