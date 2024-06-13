package com.capstone.project.tourify.data.pagging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.capstone.project.tourify.data.local.entity.CategoryEntity
import com.capstone.project.tourify.data.local.room.category.CategoryDatabase
import com.capstone.project.tourify.data.local.room.remotekeys.RemoteKeys
import com.capstone.project.tourify.data.remote.retrofit.ApiService
import com.capstone.project.tourify.data.remote.response.CategoryResponseItem
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CategoryRemoteMediator(
    private val database: CategoryDatabase,
    private val apiService: ApiService,
    private val category: String
) : RemoteMediator<Int, CategoryResponseItem>() {

    private val categoryDao = database.categoryDao()
    private val remoteKeysDao = database.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CategoryResponseItem>
    ): MediatorResult {
        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val page = loadKey ?: 1
            val categories = apiService.getCategory(category, page, state.config.pageSize)
            val endOfPaginationReached = categories.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.deleteRemoteKeys()
                    categoryDao.deleteAll()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = categories.map {
                    RemoteKeys(id = it.placeId, category = it.category, prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeysDao.insertAll(keys)
                categoryDao.insertAll(categories.map { it.toEntity() })
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, CategoryResponseItem>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { category -> remoteKeysDao.getRemoteKeysId(category.placeId, category.category) }
    }

    private fun CategoryResponseItem.toEntity(): CategoryEntity {
        return CategoryEntity(
            placeId = this.placeId,
            placeName = this.placeName,
            placeDesc = this.placeDesc,
            placeAddress = this.placeAddress,
            placePhotoUrl = this.placePhotoUrl,
            placeGmapsUrl = this.placeGmapsUrl,
            city = this.city,
            price = this.price,
            rating = this.rating,
            category = this.category
        )
    }
}
