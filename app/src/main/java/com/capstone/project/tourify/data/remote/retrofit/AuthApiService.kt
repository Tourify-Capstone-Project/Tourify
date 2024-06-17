package com.capstone.project.tourify.data.remote.retrofit

import com.capstone.project.tourify.data.remote.response.DetailUserResponse
import com.capstone.project.tourify.data.remote.response.LoginResponse
import com.capstone.project.tourify.data.remote.response.ProfileResponse
import com.capstone.project.tourify.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApiService {
    @FormUrlEncoded
    @POST("signup")
    suspend fun signUp(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("profile")
    suspend fun postProfile(
        @Part photo: MultipartBody.Part
    ): ProfileResponse

    @GET("profile")
    suspend fun getProfile(): DetailUserResponse
}