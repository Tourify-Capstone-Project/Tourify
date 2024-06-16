package com.capstone.project.tourify.data.local.room.favorite

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.capstone.project.tourify.data.local.entity.favorite.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert
    suspend fun insert(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM favorites WHERE id = :id")
    suspend fun getFavoriteById(id: String): FavoriteEntity?

    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites(): List<FavoriteEntity>
}
