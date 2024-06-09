package com.capstone.project.tourify.data.local.room

data class UserModel(
    val email: String,
    val password: String,
    val token: String,
    val displayName: String,
    val isLogin: Boolean = false
)
