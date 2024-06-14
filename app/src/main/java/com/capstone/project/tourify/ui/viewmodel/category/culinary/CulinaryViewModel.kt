package com.capstone.project.tourify.ui.viewmodel.category.culinary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.capstone.project.tourify.data.local.entity.CategoryEntity
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CulinaryViewModel(private val repository: UserRepository) : ViewModel() {

    fun getCategoriesByType(type: String): Flow<PagingData<CategoryEntity>> {
        return repository.getCategoriesByType(type).cachedIn(viewModelScope)
    }

    fun filterCategories(query: String, type: String): Flow<PagingData<CategoryEntity>> {
        return getCategoriesByType(type).map { pagingData ->
            pagingData.filter { category ->
                category.placeName.contains(query, ignoreCase = true)
            }
        }
    }
}
