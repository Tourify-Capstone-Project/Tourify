package com.capstone.project.tourify.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PhotoProfileResponse(

	@field:SerializedName("photoPublicUrl")
	val photoPublicUrl: String,

	@field:SerializedName("userEmail")
	val userEmail: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("userId")
	val userId: String
) : Parcelable
