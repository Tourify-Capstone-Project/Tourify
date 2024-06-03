package com.capstone.project.tourify.di

import android.content.Context
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.data.remote.retrofit.AuthApiConfig
import com.capstone.project.tourify.data.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object AuthInjection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val userPreference = UserPreference.getInstance(context)
        val user = runBlocking { userPreference.getSession().first() }
        val authApiService = AuthApiConfig.getAuthApiService(user.token)
        return AuthRepository(authApiService, userPreference)
    }
}