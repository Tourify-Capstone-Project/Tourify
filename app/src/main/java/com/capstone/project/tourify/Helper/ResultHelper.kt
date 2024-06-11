package com.capstone.project.tourify.Helper

sealed class ResultHelper<out T> {
    data class Success<T>(val data: T) : ResultHelper<T>()
    data class Error(val message: String) : ResultHelper<Nothing>()
    object Loading : ResultHelper<Nothing>()
}