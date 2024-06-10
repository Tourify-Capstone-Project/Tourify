package com.capstone.project.tourify.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val placeId: String,
    val placeName: String,
    val placeDesc: String,
    val placeAddress: String,
    val placePhotoUrl: String,
    val placeGmapsUrl: String,
    val city: String,
    val price: String,
    val rating: String,
    val category: String
)