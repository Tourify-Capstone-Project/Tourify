package com.capstone.project.tourify.data.remote.response

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("uid") val uid: String,
    @SerializedName("email") val email: String,
    @SerializedName("emailVerified") val emailVerified: Boolean,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("profilePictureUrl") val profilePictureUrl: String,
    @SerializedName("isAnonymous") val isAnonymous: Boolean,
    @SerializedName("providerData") val providerData: List<ProviderData>,
    @SerializedName("stsTokenManager") val stsTokenManager: StsTokenManager,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("lastLoginAt") val lastLoginAt: String,
    @SerializedName("apiKey") val apiKey: String,
    @SerializedName("appName") val appName: String
)

data class ProviderData(
    @SerializedName("providerId") val providerId: String,
    @SerializedName("uid") val uid: String,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phoneNumber") val phoneNumber: String?,
    @SerializedName("photoURL") val photoURL: String?
)

data class StsTokenManager(
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("expirationTime") val expirationTime: Long
)

data class LoginResponse(
    @SerializedName("message") val message: String,
    @SerializedName("user") val user: User,
    @SerializedName("token") val token: String
)
