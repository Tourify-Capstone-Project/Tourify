package com.capstone.project.tourify.data.local.room.category

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.project.tourify.data.local.entity.category.CategoryEntity

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories WHERE category = :categoryType")
    fun getCategoriesByType(categoryType: String): LiveData<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)
}
