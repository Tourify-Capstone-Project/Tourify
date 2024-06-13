package com.capstone.project.tourify.data.local.room.remotekeys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val id: String,
    val category: String,
    val prevKey: Int?,
    val nextKey: Int?
)