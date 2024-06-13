package com.capstone.project.tourify.data.local.room.category

import androidx.paging.PagingSource
import androidx.room.*
import com.capstone.project.tourify.data.local.entity.CategoryEntity
import com.capstone.project.tourify.data.remote.response.CategoryResponseItem

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Query("SELECT * FROM categories WHERE category = :category")
    fun getCategoriesByType(category: String): PagingSource<Int, CategoryResponseItem>

    @Query("DELETE FROM categories")
    suspend fun deleteAll()
}
