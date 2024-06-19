package com.capstone.project.tourify.data.local.entity

data class UserModel(
    val userId: String,
    val email: String,
    val password: String,
    val token: String,
    val displayName: String,
    val isLogin: Boolean = false
)

