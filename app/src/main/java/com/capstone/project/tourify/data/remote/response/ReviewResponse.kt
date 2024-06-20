package com.capstone.project.tourify.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ReviewResponse(

	@field:SerializedName("reviews")
	val reviews: List<ReviewsItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("status")
	val status: String
) : Parcelable

@Parcelize
data class ReviewsItem(

	@field:SerializedName("review_id")
	val reviewId: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("photoPublicUrl")
	val photoPublicUrl: String,

	@field:SerializedName("review_desc")
	val reviewDesc: String,

	@field:SerializedName("username")
	val username: String
) : Parcelable
