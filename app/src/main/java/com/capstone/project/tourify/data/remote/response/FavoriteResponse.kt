package com.capstone.project.tourify.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class FavoriteResponse(

	@field:SerializedName("detailsFavorite")
	val detailsFavorite: List<DetailsFavoriteItem>
) : Parcelable

@Parcelize
data class DetailPlace(

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("rating")
	val rating: String,

	@field:SerializedName("placeName")
	val placeName: String,

	@field:SerializedName("placePhotoUrl")
	val placePhotoUrl: String
) : Parcelable

@Parcelize
data class DetailsFavoriteItem(

	@field:SerializedName("detailPlace")
	val detailPlace: DetailPlace,

	@field:SerializedName("favoriteId")
	val favoriteId: String,

	@field:SerializedName("tourismId")
	val tourismId: String,

	@field:SerializedName("userEmail")
	val userEmail: String,

	@field:SerializedName("userId")
	val userId: String
) : Parcelable
