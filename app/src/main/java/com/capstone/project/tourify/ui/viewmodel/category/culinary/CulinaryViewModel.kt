package com.capstone.project.tourify.ui.viewmodel.category.culinary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.capstone.project.tourify.data.local.entity.CategoryEntity
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CulinaryViewModel(private val userRepository: UserRepository) : ViewModel() {

    // LiveData to hold the filtered categories
    private val _filteredCategories = MutableLiveData<PagingData<CategoryEntity>>()
    val filteredCategories: LiveData<PagingData<CategoryEntity>> = _filteredCategories

    // Function to get categories as PagingData
    fun getCategoriesByType(category: String): Flow<PagingData<CategoryEntity>> {
        return userRepository.getCategoriesByType(category)
            .cachedIn(viewModelScope)
    }

    // Function to filter categories based on query
    fun filterCategories(query: String, category: String) {
        viewModelScope.launch {
            getCategoriesByType(category)
                .map { pagingData ->
                    pagingData.filter { item ->
                        item.placeName.contains(query, ignoreCase = true) ||
                                item.placeId.contains(query, ignoreCase = true)
                    }
                }.collectLatest { filteredPagingData ->
                    _filteredCategories.value = filteredPagingData
                }
        }
    }
}
