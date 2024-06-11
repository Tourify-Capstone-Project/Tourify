package com.capstone.project.tourify.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.SerializedName

@Parcelize
data class ArticlesResponse(
	@field:SerializedName("ArticlesResponse")
	val articlesResponse: List<ArticlesResponseItem>
) : Parcelable

@Parcelize
@Entity(tableName = "article")
data class ArticlesResponseItem(
	@PrimaryKey
	@field:SerializedName("articleId")
	val articleId: String,

	@field:SerializedName("articleTitle")
	val articleTitle: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("articlePhotoUrl")
	val articlePhotoUrl: String,

	@field:SerializedName("articleDesc")
	val articleDesc: String,

	@field:SerializedName("articleUrl")
	val articleUrl: String
) : Parcelable
