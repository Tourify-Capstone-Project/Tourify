package com.capstone.project.tourify.data.remote.retrofit

import com.capstone.project.tourify.data.remote.response.AllDestinationResponseItem
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.remote.response.CategoryResponseItem
import com.capstone.project.tourify.data.remote.response.DetailResponse

import com.capstone.project.tourify.data.remote.response.FavoriteResponse
import com.capstone.project.tourify.data.remote.response.FinanceResponse
import com.capstone.project.tourify.data.remote.response.PhotoProfileResponse
import com.capstone.project.tourify.data.remote.response.ReviewResponse
import com.capstone.project.tourify.data.remote.response.ReviewUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.*

interface ApiService {

    @GET("home")
    suspend fun getArticles(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): List<ArticlesResponseItem>

    @GET("category/{category}")
    suspend fun getCategory(
        @Path("category") category: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): List<CategoryResponseItem>

    @GET("destination/{tourism_id}")
    suspend fun getDetail(
        @Path("tourism_id") tourismId: String
    ): Response<DetailResponse>

    @POST("destination/{tourism_id}/favorite-destination")
    suspend fun addFavorite(
        @Path("tourism_id") tourismId: String
    ): Response<DetailResponse>

    @DELETE("destination/{tourism_id}/favorite-destination")
    suspend fun removeFavorite(
        @Path("tourism_id") tourismId: String
    ): Response<DetailResponse>

    @GET("favorite-destination")
    suspend fun getFavoriteDestinations(): Response<FavoriteResponse>

    @GET("finance-destination")
    suspend fun getFinanceDestinations(): Response<FinanceResponse>

    @GET("all-destination")
    suspend fun searchDestinations(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): List<AllDestinationResponseItem>

    @GET("destination/{tourism_id}/review-destination")
    suspend fun getReviews(
        @Path("tourism_id") tourismId: String
    ): ReviewResponse

    @FormUrlEncoded
    @POST("destination/{tourismId}/review-destination")
    suspend fun submitReview(
        @Path("tourismId") tourismId: String,
        @Field("review") review: String
    ): ReviewUserResponse

    @PUT("profile")
    @FormUrlEncoded
    suspend fun updateUsername(
        @Field("username") newUsername: String
    ): PhotoProfileResponse

    @Multipart
    @POST("profile")
    suspend fun uploadPhoto(
        @Part imgProfile: MultipartBody.Part
    ): PhotoProfileResponse
}
