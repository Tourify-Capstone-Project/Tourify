package com.capstone.project.tourify.data.local.room.category

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.project.tourify.data.local.entity.category.CategoryEntity
import com.capstone.project.tourify.data.remote.response.AllDestinationResponseItem
import com.capstone.project.tourify.data.remote.response.CategoryResponseItem

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Query("SELECT * FROM categories WHERE category = :category")
    fun getCategoriesByType(category: String): PagingSource<Int, CategoryResponseItem>

    @Query("SELECT * FROM categories")
    fun getAllPlace(): PagingSource<Int, AllDestinationResponseItem>

    @Query("DELETE FROM categories")
    suspend fun deleteAll()

}
