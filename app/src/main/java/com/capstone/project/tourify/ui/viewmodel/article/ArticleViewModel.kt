package com.capstone.project.tourify.ui.viewmodel.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.repository.UserRepository

class ArticleViewModel(userRepository: UserRepository) : ViewModel() {

    val getHeadlineNews: LiveData<PagingData<ArticlesResponseItem>> =
        userRepository.getArticles().cachedIn(viewModelScope)

}
