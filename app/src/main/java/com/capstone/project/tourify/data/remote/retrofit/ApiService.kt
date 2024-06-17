package com.capstone.project.tourify.data.remote.retrofit

import com.capstone.project.tourify.data.remote.response.AllDestinationResponseItem
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.remote.response.CategoryResponseItem
import com.capstone.project.tourify.data.remote.response.DetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        @Path("tourism_id") tourismId: String,
    ): DetailResponse

    @GET("all-destination")
    suspend fun searchDestinations(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): List<AllDestinationResponseItem>
}
