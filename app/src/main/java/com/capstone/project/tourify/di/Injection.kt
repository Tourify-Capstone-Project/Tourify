package com.capstone.project.tourify.di

import android.content.Context
import com.capstone.project.tourify.data.local.room.article.ArticleDatabase
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.data.remote.pref.dataStore
import com.capstone.project.tourify.data.remote.retrofit.ApiConfig
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig().getApiService(user.token)
        val articleDatabase = ArticleDatabase.getDatabase(context)
        return UserRepository.getInstance(apiService, articleDatabase)
    }
}