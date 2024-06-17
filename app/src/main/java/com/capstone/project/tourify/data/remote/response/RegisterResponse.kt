package com.capstone.project.tourify.data.remote.response

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class RegisterResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("user")
    val user: Users,

    @field:SerializedName("status")
    val status: String
) : Parcelable

@Parcelize
data class Metadata(

    @field:SerializedName("lastSignInTime")
    val lastSignInTime: String,

    @field:SerializedName("creationTime")
    val creationTime: String,

    @field:SerializedName("lastRefreshTime")
    val lastRefreshTime: String
) : Parcelable

@Parcelize
data class ProviderDataItem(

    @field:SerializedName("uid")
    val uid: String,

    @field:SerializedName("displayName")
    val displayName: String,

    @field:SerializedName("providerId")
    val providerId: String,

    @field:SerializedName("email")
    val email: String
) : Parcelable

@Parcelize
data class Users(

    @field:SerializedName("uid")
    val uid: String,

    @field:SerializedName("emailVerified")
    val emailVerified: Boolean,

    @field:SerializedName("metadata")
    val metadata: Metadata,

    @field:SerializedName("providerData")
    val providerData: List<ProviderDataItem>,

    @field:SerializedName("displayName")
    val displayName: String,

    @field:SerializedName("disabled")
    val disabled: Boolean,

    @field:SerializedName("tokensValidAfterTime")
    val tokensValidAfterTime: String,

    @field:SerializedName("email")
    val email: String
) : Parcelable
