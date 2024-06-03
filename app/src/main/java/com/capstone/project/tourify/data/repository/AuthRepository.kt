package com.capstone.project.tourify.data.repository

import com.capstone.project.tourify.data.local.room.UserModel
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.data.remote.response.LoginResponse
import com.capstone.project.tourify.data.remote.response.RegisterResponse
import com.capstone.project.tourify.data.remote.retrofit.AuthApiService

class AuthRepository(
    private val authApiService: AuthApiService,
    private val userPreference: UserPreference
) {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return authApiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val response = authApiService.login(email, password)
        if (!response.error!!) {
            response.loginResult?.token?.let { token ->
                userPreference.saveSession(UserModel(email, token, true))
            }
        }
        return response
    }

    suspend fun saveSession(userModel: UserModel) {
        userPreference.saveSession(userModel)
    }

    companion object {
        fun getInstance(authApiService: AuthApiService, userPreference: UserPreference) =
            AuthRepository(authApiService, userPreference)
    }
}