package com.capstone.project.tourify.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.project.tourify.data.remote.response.DetailResponse

@Dao
interface DetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetail(detail: DetailResponse)

    @Query("SELECT * FROM detailresponse WHERE placeId = :placeId")
    suspend fun getDetailById(placeId: String): DetailResponse?
}
