package com.capstone.project.tourify.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Parcelize
data class AllDestinationResponse(

	@field:SerializedName("AllDestinationResponse")
	val allDestinationResponse: List<AllDestinationResponseItem>
) : Parcelable

@Parcelize
data class AllDestinationResponseItem(

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
) : Parcelable
