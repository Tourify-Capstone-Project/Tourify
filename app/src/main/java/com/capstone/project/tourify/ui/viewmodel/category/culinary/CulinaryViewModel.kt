package com.capstone.project.tourify.ui.viewmodel.category.culinary

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.project.tourify.data.local.entity.CategoryEntity
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.launch

class CulinaryViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getCategoriesByType(category: String): LiveData<PagingData<CategoryEntity>> {
        return userRepository.getCategoriesByType(category)
            .cachedIn(viewModelScope)
    }

    fun refreshCategories(category: String) {
        viewModelScope.launch {
            userRepository.refreshCategories(category)
        }
    }
}
