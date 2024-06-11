package com.capstone.project.tourify.data.remote.retrofit

import com.capstone.project.tourify.data.remote.response.ArticlesResponse
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import retrofit2.http.*

interface ApiService {
    @GET("home")
    suspend fun getArticles(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): List<ArticlesResponseItem>
}