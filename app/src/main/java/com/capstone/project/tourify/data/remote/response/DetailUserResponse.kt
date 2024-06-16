package com.capstone.project.tourify.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DetailUserResponse(

	@field:SerializedName("detailsUser")
	val detailsUser: DetailsUser,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("status")
	val status: String
) : Parcelable

@Parcelize
data class DetailsUser(

	@field:SerializedName("user_email")
	val userEmail: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("username")
	val username: String
) : Parcelable