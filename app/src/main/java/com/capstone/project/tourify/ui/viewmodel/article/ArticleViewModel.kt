package com.capstone.project.tourify.ui.viewmodel.article

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

class ArticleViewModel(articlesRepository: UserRepository) : ViewModel() {

    val getHeadlineNews: LiveData<PagingData<ArticlesResponseItem>> =
        articlesRepository.getArticles().cachedIn(viewModelScope)
}
