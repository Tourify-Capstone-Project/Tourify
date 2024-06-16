package com.capstone.project.tourify.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.liveData
import com.capstone.project.tourify.data.local.entity.CategoryEntity
import com.capstone.project.tourify.data.local.room.article.ArticleDatabase
import com.capstone.project.tourify.data.local.room.category.CategoryDao
import com.capstone.project.tourify.data.local.room.category.CategoryDatabase
import com.capstone.project.tourify.data.local.room.detail.DetailDao
import com.capstone.project.tourify.data.pagging.ArticleRemoteMediator
import com.capstone.project.tourify.data.pagging.CategoryRemoteMediator
import com.capstone.project.tourify.data.pagging.DestinationRemoteMediator
import com.capstone.project.tourify.data.remote.retrofit.ApiService
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.remote.response.DetailResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(
    private val apiService: ApiService,
    private val categoryDao: CategoryDao,
    private val detailDao: DetailDao,
    private val articleDatabase: ArticleDatabase,
    private val categoryDatabase: CategoryDatabase
) {

    // Category Methods
    @OptIn(ExperimentalPagingApi::class)
    fun getCategoriesByType(category: String): Flow<PagingData<CategoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            remoteMediator = CategoryRemoteMediator(categoryDatabase, apiService, category),
            pagingSourceFactory = { categoryDao.getCategoriesByType(category) }
        ).flow.map { pagingData ->
            pagingData.map { categoryResponseItem ->
                CategoryEntity(
                    placeId = categoryResponseItem.placeId,
                    placeName = categoryResponseItem.placeName,
                    placeDesc = categoryResponseItem.placeDesc,
                    placeAddress = categoryResponseItem.placeAddress,
                    placePhotoUrl = categoryResponseItem.placePhotoUrl,
                    placeGmapsUrl = categoryResponseItem.placeGmapsUrl,
                    city = categoryResponseItem.city,
                    price = categoryResponseItem.price,
                    rating = categoryResponseItem.rating,
                    category = categoryResponseItem.category
                )
            }
        }
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

    @OptIn(ExperimentalPagingApi::class)
    fun getArticles(category: String): LiveData<PagingData<ArticlesResponseItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            remoteMediator = ArticleRemoteMediator(articleDatabase, apiService, category),
            pagingSourceFactory = { articleDatabase.articleDao().getAllArticle() }
        ).liveData
    }

    // Destination Methods
    @OptIn(ExperimentalPagingApi::class)
    fun searchDestinations(category: String): Flow<PagingData<CategoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            remoteMediator = DestinationRemoteMediator(categoryDatabase, apiService, category),
            pagingSourceFactory = { categoryDao.getAllPlace() }
        ).flow.map { pagingData ->
            pagingData.map { categoryResponseItem ->
                CategoryEntity(
                    placeId = categoryResponseItem.placeId,
                    placeName = categoryResponseItem.placeName,
                    placeDesc = categoryResponseItem.placeDesc,
                    placeAddress = categoryResponseItem.placeAddress,
                    placePhotoUrl = categoryResponseItem.placePhotoUrl,
                    placeGmapsUrl = categoryResponseItem.placeGmapsUrl,
                    city = categoryResponseItem.city,
                    price = categoryResponseItem.price,
                    rating = categoryResponseItem.rating,
                    category = categoryResponseItem.category
                )
            }
        }
    }

    companion object {
        fun getInstance(
            apiService: ApiService,
            categoryDao: CategoryDao,
            detailDao: DetailDao,
            articleDatabase: ArticleDatabase,
            categoryDatabase: CategoryDatabase,
        ) = UserRepository(apiService, categoryDao, detailDao, articleDatabase, categoryDatabase)
    }
}