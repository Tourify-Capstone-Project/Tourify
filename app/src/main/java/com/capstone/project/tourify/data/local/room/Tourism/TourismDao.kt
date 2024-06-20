package com.capstone.project.tourify.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.project.tourify.data.local.entity.RecommendedItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TourismDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRecommended(items: List<RecommendedItem>)

    @Query("SELECT * FROM recommended_items")
    fun getAllRecommended(): Flow<List<RecommendedItem>>
}
