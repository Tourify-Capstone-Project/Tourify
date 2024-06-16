package com.capstone.project.tourify.data.local.room.detail

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.project.tourify.data.remote.response.DetailResponse
import com.capstone.project.tourify.data.local.entity.favorite.FavoriteEntity

@Dao
interface DetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetail(detail: DetailResponse)

    @Query("SELECT * FROM detailresponse WHERE placeId = :placeId")
    suspend fun getDetailById(placeId: String): DetailResponse?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun removeFavoriteById(id: String)

    @Query("SELECT * FROM favorites WHERE id = :id")
    suspend fun getFavoriteById(id: String): FavoriteEntity?
}
