package com.capstone.project.tourify.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.project.tourify.data.repository.AuthRepository
import com.capstone.project.tourify.di.AuthInjection
import com.capstone.project.tourify.ui.viewmodel.login.LoginViewModel
import com.capstone.project.tourify.ui.viewmodel.profile.ProfileViewModel
import com.capstone.project.tourify.ui.viewmodel.register.RegisterViewModel

class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) =
            AuthViewModelFactory(AuthInjection.provideAuthRepository(context))
    }
}