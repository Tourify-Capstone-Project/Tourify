package com.capstone.project.tourify.data.repository

import androidx.lifecycle.LiveData
import com.capstone.project.tourify.data.local.entity.CategoryEntity
import com.capstone.project.tourify.data.local.room.CategoryDao
import com.capstone.project.tourify.data.local.room.DetailDao
import com.capstone.project.tourify.data.remote.response.CategoryResponseItem
import com.capstone.project.tourify.data.remote.response.DetailResponse
import com.capstone.project.tourify.data.remote.retrofit.ApiService

class UserRepository(
    private val apiService: ApiService,
    private val categoryDao: CategoryDao,
    private val detailDao: DetailDao
) {

    // Category Methods
    fun getCategoriesByType(category: String): LiveData<List<CategoryEntity>> {
        return categoryDao.getCategoriesByType(category)
    }

    suspend fun refreshCategories(category: String): List<CategoryEntity> {
        val response: List<CategoryResponseItem> = apiService.getCategory(category)
        val categories = response.map {
            CategoryEntity(
                placeId = it.placeId,
                placeName = it.placeName,
                placeDesc = it.placeDesc,
                placeAddress = it.placeAddress,
                placePhotoUrl = it.placePhotoUrl,
                placeGmapsUrl = it.placeGmapsUrl,
                city = it.city,
                price = it.price,
                rating = it.rating,
                category = it.category
            )
        }
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
}
