	package com.capstone.project.tourify.data.remote.response

	import kotlinx.parcelize.Parcelize
	import android.os.Parcelable
	import com.google.gson.annotations.SerializedName

	@Parcelize
	data class RecommendedResponse(

		@field:SerializedName("detailsFavorite")
		val detailsFavorite: List<DetailsItemRecommended>
	) : Parcelable

	@Parcelize
	data class DetailsItemRecommended(

		@field:SerializedName("recomenId")
		val recomenId: String,

		@field:SerializedName("detailPlace")
		val detailPlace: DetailPlace,

		@field:SerializedName("tourismId")
		val tourismId: String,

		@field:SerializedName("userEmail")
		val userEmail: String,

		@field:SerializedName("userId")
		val userId: String
	) : Parcelable

	@Parcelize
	data class DetailPlaceRecommended(

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
