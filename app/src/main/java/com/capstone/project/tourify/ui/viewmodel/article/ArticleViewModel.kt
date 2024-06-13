package com.capstone.project.tourify.ui.viewmodel.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.repository.UserRepository

class ArticleViewModel(private val articlesRepository: UserRepository, private val category: String = "") : ViewModel() {

    val getHeadlineNews: LiveData<PagingData<ArticlesResponseItem>> =
        articlesRepository.getArticles(category).cachedIn(viewModelScope)
}
