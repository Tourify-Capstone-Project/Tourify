package com.capstone.project.tourify.data.local.entity.category

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.capstone.project.tourify.data.remote.response.AllDestinationResponseItem

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

fun AllDestinationResponseItem.toEntity(): CategoryEntity {
    return CategoryEntity(
        placeDesc = this.placeDesc,
        city = this.city,
        price = this.price,
        placeId = this.placeId,
        rating = this.rating,
        placeAddress = this.placeAddress,
        placeGmapsUrl = this.placeGmapsUrl,
        category = this.category,
        placeName = this.placeName,
        placePhotoUrl = this.placePhotoUrl
    )
}