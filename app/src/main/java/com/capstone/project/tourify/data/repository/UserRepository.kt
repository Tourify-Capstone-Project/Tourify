package com.capstone.project.tourify.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.capstone.project.tourify.data.local.room.article.ArticleDatabase
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.remote.retrofit.ApiService
import com.capstone.project.tourify.data.local.entity.CategoryEntity
import com.capstone.project.tourify.data.local.room.category.CategoryDao
import com.capstone.project.tourify.data.local.room.detail.DetailDao
import com.capstone.project.tourify.data.pagging.ArticleRemoteMediator
import com.capstone.project.tourify.data.remote.response.DetailResponse

class UserRepository(
    private val apiService: ApiService,
    private val categoryDao: CategoryDao,
    private val detailDao: DetailDao,
    private val articleDatabase: ArticleDatabase
) {

    // Category Methods
    fun getCategoriesByType(category: String): LiveData<List<CategoryEntity>> {
        return categoryDao.getCategoriesByType(category)
    }

    suspend fun refreshCategories(category: String): List<CategoryEntity> {
        val response = apiService.getCategory(category)
        val categories = response.map { it.toEntity() }
        categoryDao.insertAll(categories)
        return categories
    }


    suspend fun insertCategories(categories: List<CategoryEntity>) {
        categoryDao.insertAll(categories)
    }

    // Detail Methods
    suspend fun getDetail(tourismId: String): DetailResponse? {
        return try {
            val detail = apiService.getDetail(tourismId)
            detailDao.insertDetail(detail)
            detail
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun saveDetail(detail: DetailResponse) {
        detailDao.insertDetail(detail)
    }

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
            categoryDao: CategoryDao,
            detailDao: DetailDao,
            articleDatabase: ArticleDatabase
        ) = UserRepository(apiService, categoryDao, detailDao, articleDatabase)
    }
}