package com.capstone.project.tourify.ui.viewmodel.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.project.tourify.data.local.entity.category.CategoryEntity
import androidx.paging.PagingData
import androidx.paging.filter
import com.capstone.project.tourify.data.repository.UserRepository

class CategoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val _categories = MutableLiveData<PagingData<CategoryEntity>>()
    val categories: LiveData<PagingData<CategoryEntity>> get() = _categories

    private val _filteredCategories = MutableLiveData<PagingData<CategoryEntity>>()
    val filteredCategories: LiveData<PagingData<CategoryEntity>> get() = _filteredCategories

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val currentCategory = MutableLiveData<String>()

    init {
        _categories.value = PagingData.empty()
        _filteredCategories.value = PagingData.empty()
    }

//    fun getCategoriesByType(category: String) {
//        _isLoading.value = true
//        currentCategory.value = category
//        currentCategory.switchMap {
//            repository.getCategoriesByType(it).cachedIn(viewModelScope)
//        }.observeForever { pagingData ->
//            _categories.value = pagingData
//            _filteredCategories.value = pagingData
//            _isLoading.value = false
//        }
//    }

//    fun refreshCategories(category: String) {
//        _isLoading.value = true
//        viewModelScope.launch {
//            try {
//                val categories = repository.refreshCategories(category)
//                _categories.value = PagingData.from(categories)
//                _filteredCategories.value = PagingData.from(categories)
//            } catch (e: Exception) {
//                _error.value = e.message
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

    fun filterCategories(query: String) {
        val filteredList = _categories.value?.filter {
            it.placeName.contains(query, ignoreCase = true) || it.placeId.contains(query, ignoreCase = true)
        } ?: PagingData.empty()
        _filteredCategories.value = filteredList
    }
}
