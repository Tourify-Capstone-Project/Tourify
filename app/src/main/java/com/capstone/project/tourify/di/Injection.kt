package com.capstone.project.tourify.di

import android.content.Context
import com.capstone.project.tourify.data.local.room.article.ArticleDatabase
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.data.remote.pref.dataStore
import com.capstone.project.tourify.data.local.room.CategoryDatabase
import com.capstone.project.tourify.data.local.room.DetailDatabase
import com.capstone.project.tourify.data.remote.retrofit.ApiConfig
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context)
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)

        //Article
        val articleDatabase = ArticleDatabase.getDatabase(context)

        //Category
        val categoryDatabase = CategoryDatabase.getDatabase(context)
        val categoryDao = categoryDatabase.categoryDao()

        //Detail
        val detailDatabase = DetailDatabase.getDatabase(context)
        val detailDao = detailDatabase.detailDao()

        return UserRepository(apiService, categoryDao, detailDao, articleDatabase, categoryDatabase)
    }
}
