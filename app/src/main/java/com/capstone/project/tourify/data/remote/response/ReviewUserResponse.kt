package com.capstone.project.tourify.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ReviewUserResponse(

	@field:SerializedName("message")
	val message: String
) : Parcelable
