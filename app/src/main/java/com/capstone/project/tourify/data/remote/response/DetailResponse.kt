package com.capstone.project.tourify.data.remote.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.capstone.project.tourify.Helper.ConverterHelper
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "detailresponse")
@TypeConverters(ConverterHelper::class)
@Parcelize
data class DetailResponse(

	@PrimaryKey
	@field:SerializedName("placeId")
	val placeId: String,

	@field:SerializedName("placeDesc")
	val placeDesc: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("additionalImages")
	val additionalImages: List<AdditionalImagesItem>,

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
) : Parcelable

@Parcelize
data class AdditionalImagesItem(

	@field:SerializedName("tourism_id")
	val tourismId: String,

	@field:SerializedName("image_id")
	val imageId: String,

	@field:SerializedName("url_image")
	val urlImage: String
) : Parcelable
