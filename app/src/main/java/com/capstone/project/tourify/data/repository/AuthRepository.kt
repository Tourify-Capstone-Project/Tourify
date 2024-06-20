package com.capstone.project.tourify.data.repository

import com.capstone.project.tourify.data.local.entity.UserModel
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.data.remote.response.LoginResponse
import com.capstone.project.tourify.data.remote.response.RegisterResponse
import com.capstone.project.tourify.data.remote.retrofit.AuthApiService
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val authApiService: AuthApiService,
    private val userPreference: UserPreference
) {

    suspend fun signUp(username: String, email: String, password: String): RegisterResponse {
        return authApiService.signUp(username, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val response = authApiService.login(email, password)
        if (response.message.isNotEmpty() && response.token.isNotEmpty()) {
            val userModel = UserModel(
                userId = response.user.uid,
                email = email,
                password = password,
                token = response.token,
                displayName = response.user.displayName,
                isLogin = true
            )

            userPreference.saveSession(userModel)
        }
        return response
    }

    suspend fun saveSession(userModel: UserModel) {
        userPreference.saveSession(userModel)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }
    companion object {
        fun getInstance(
            userPreference: UserPreference,
            apiService: AuthApiService
        ) = AuthRepository(apiService, userPreference)
    }
}
