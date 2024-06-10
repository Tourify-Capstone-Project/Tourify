package com.capstone.project.tourify.data.remote.retrofit

import com.capstone.project.tourify.data.remote.response.CategoryResponseItem
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("category/{category}")
    suspend fun getCategory(
        @Path("category") category: String
    ): List<CategoryResponseItem>  // Mengubah dari CategoryResponse ke List<CategoryResponseItem>
}
