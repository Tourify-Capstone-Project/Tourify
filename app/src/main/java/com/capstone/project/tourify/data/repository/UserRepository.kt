package com.capstone.project.tourify.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.capstone.project.tourify.data.local.room.article.ArticleDatabase
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.remote.retrofit.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.ArticleRemoteMediator

class UserRepository private constructor(
    private val apiService: ApiService,
    private val articleDatabase: ArticleDatabase
) {
    fun getArticles(): LiveData<PagingData<ArticlesResponseItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = ArticleRemoteMediator(articleDatabase, apiService),
            pagingSourceFactory = {
                articleDatabase.articleDao().getAllArticle()
            }
        ).liveData
    }

    companion object {
        fun getInstance(
            apiService: ApiService,
            articleDatabase: ArticleDatabase
        ) = UserRepository(apiService, articleDatabase)
    }
}