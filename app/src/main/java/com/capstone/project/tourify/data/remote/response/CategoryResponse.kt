package com.capstone.project.tourify.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.capstone.project.tourify.data.local.entity.category.CategoryEntity
import com.google.gson.annotations.SerializedName

@Parcelize
data class CategoryResponseItem(

    @field:SerializedName("placeDesc")
    val placeDesc: String,

    @field:SerializedName("city")
    val city: String,

    @field:SerializedName("price")
    val price: String,

    @field:SerializedName("placeId")
    val placeId: String,

    @field:SerializedName("rating")
    val rating: String,

    @field:SerializedName("placeAddress")
    val placeAddress: String,

    @field:SerializedName("placeGmapsUrl")
    val placeGmapsUrl: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("placeName")
    val placeName: String,

    @field:SerializedName("placePhotoUrl")
    val placePhotoUrl: String
) : Parcelable {
    fun toEntity(): CategoryEntity {
        return CategoryEntity(
            placeId = placeId,
            placeName = placeName,
            placeDesc = placeDesc,
            placeAddress = placeAddress,
            placePhotoUrl = placePhotoUrl,
            placeGmapsUrl = placeGmapsUrl,
            city = city,
            price = price,
            rating = rating,
            category = category
        )
    }
}