package com.capstone.project.tourify.data.remote.retrofit

import com.capstone.project.tourify.data.remote.response.CategoryResponseItem
import com.capstone.project.tourify.data.remote.response.DetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("category/{category}")
    suspend fun getCategory(
        @Path("category") category: String
    ): List<CategoryResponseItem>  // Mengubah dari CategoryResponse ke List<CategoryResponseItem>

    @GET("destination/{tourism_id}")
    suspend fun getDetail(
        @Path("tourism_id") tourismId: String
    ): DetailResponse
}
