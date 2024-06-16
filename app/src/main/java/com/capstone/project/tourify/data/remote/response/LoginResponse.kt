package com.capstone.project.tourify.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("user")
    val user: User,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("token")
    val token: String
) : Parcelable

@Parcelize
data class User(

    @field:SerializedName("uid")
    val uid: String,

    @field:SerializedName("emailVerified")
    val emailVerified: Boolean,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("isAnonymous")
    val isAnonymous: Boolean,

    @field:SerializedName("stsTokenManager")
    val stsTokenManager: StsTokenManager,

    @field:SerializedName("lastLoginAt")
    val lastLoginAt: String,

    @field:SerializedName("apiKey")
    val apiKey: String,

    @field:SerializedName("providerData")
    val providerData: List<ProviderData>,

    @field:SerializedName("displayName")
    val displayName: String,

    @field:SerializedName("appName")
    val appName: String,

    @field:SerializedName("email")
    val email: String
) : Parcelable

@Parcelize
data class ProviderData(

    @field:SerializedName("uid")
    val uid: String,

    @field:SerializedName("photoURL")
    val photoURL: String,

    @field:SerializedName("phoneNumber")
    val phoneNumber: String,

    @field:SerializedName("providerId")
    val providerId: String,

    @field:SerializedName("displayName")
    val displayName: String,

    @field:SerializedName("email")
    val email: String
) : Parcelable

@Parcelize
data class StsTokenManager(

    @field:SerializedName("expirationTime")
    val expirationTime: Long,

    @field:SerializedName("accessToken")
    val accessToken: String,

    @field:SerializedName("refreshToken")
    val refreshToken: String
) : Parcelable