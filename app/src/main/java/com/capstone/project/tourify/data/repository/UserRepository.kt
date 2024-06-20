package com.capstone.project.tourify.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.capstone.project.tourify.data.local.entity.UserModel
import com.capstone.project.tourify.data.local.room.article.ArticleDatabase
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.remote.retrofit.ApiService
import com.capstone.project.tourify.data.local.entity.category.CategoryEntity
import com.capstone.project.tourify.data.local.entity.category.toEntity
import com.capstone.project.tourify.data.local.entity.favorite.FavoriteEntity
import com.capstone.project.tourify.data.local.room.category.CategoryDao
import com.capstone.project.tourify.data.local.room.category.CategoryDatabase
import com.capstone.project.tourify.data.local.room.detail.DetailDao
import com.capstone.project.tourify.data.local.room.favorite.FavoriteDao
import com.capstone.project.tourify.data.local.room.finance.FinanceDao
import com.capstone.project.tourify.data.pagging.ArticleRemoteMediator
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.data.remote.response.DetailResponse
import com.capstone.project.tourify.data.remote.response.FavoriteResponse
import com.capstone.project.tourify.data.remote.response.FinanceResponse
import com.capstone.project.tourify.data.remote.response.ReviewResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class UserRepository(
    private val apiService: ApiService,
    private val categoryDao: CategoryDao,
    private val detailDao: DetailDao,
    private val articleDatabase: ArticleDatabase,
    private val favoriteDao: FavoriteDao,
    private val categoryDatabase: CategoryDatabase,
    private val financeDao: FinanceDao,
    private val userPreference: UserPreference
) {

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }
    // Category Methods
    fun getCategoriesByType(category: String): LiveData<List<CategoryEntity>> {
        return categoryDao.getCategoriesByType(category)
    }

    suspend fun refreshCategories(category: String) {
        val response = apiService.getCategory(category)
        val categories = response.map { it.toEntity() }
        categoryDao.insertAll(categories)
    }

    suspend fun insertCategories(categories: List<CategoryEntity>) {
        categoryDao.insertAll(categories)
    }

    // Detail Methods
    suspend fun getDetail(tourismId: String): DetailResponse? {
        return try {
            val response = apiService.getDetail(tourismId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getReviews(tourismId: String): ReviewResponse {
        return apiService.getReviews(tourismId)
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

    suspend fun searchDestinations(query: String): List<CategoryEntity> {
        val response = apiService.searchDestinations(query)
        return response.map { it.toEntity() }
    }

    suspend fun getAllFavorites(): List<FavoriteEntity> {
        return favoriteDao.getAllFavorites()
    }

    suspend fun addFavorite(favorite: FavoriteEntity) {
        detailDao.insertFavorite(favorite)
    }

    suspend fun removeFavoriteById(id: String) {
        detailDao.removeFavoriteById(id)
    }

    suspend fun getFavoriteById(id: String): FavoriteEntity? {
        return detailDao.getFavoriteById(id)
    }


    suspend fun getFavoriteDestinations(): FavoriteResponse? {
        return try {
            val response = apiService.getFavoriteDestinations()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun addFavoriteToRemote(tourismId: String): Response<DetailResponse> {
        return apiService.addFavorite(tourismId)
    }

    suspend fun removeFavoriteFromRemote(tourismId: String): Response<DetailResponse> {
        return apiService.removeFavorite(tourismId)
    }

    suspend fun getFinanceDestinations(): FinanceResponse? {
        return try {
            val response = apiService.getFinanceDestinations()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        fun getInstance(
            apiService: ApiService,
            categoryDao: CategoryDao,
            detailDao: DetailDao,
            articleDatabase: ArticleDatabase,
            favoriteDao: FavoriteDao,
            categoryDatabase: CategoryDatabase,
            financeDao: FinanceDao,
            userPreference: UserPreference
        ) = UserRepository(apiService, categoryDao, detailDao, articleDatabase, favoriteDao, categoryDatabase, financeDao, userPreference)
    }
}
