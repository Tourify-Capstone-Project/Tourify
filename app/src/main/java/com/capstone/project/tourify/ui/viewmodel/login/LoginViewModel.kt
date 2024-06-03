package com.capstone.project.tourify.ui.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.project.tourify.data.local.room.UserModel
import com.capstone.project.tourify.data.remote.response.LoginResponse
import com.capstone.project.tourify.data.repository.AuthRepository
import com.capstone.project.tourify.utils.EspressoIdlingResource
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                _loginResult.value = response
                if (!response.error!!) {
                    val token = response.loginResult?.token
                    token?.let {
                        val userModel = UserModel(email, it, true)
                        saveSession(userModel)
                    }
                } else {
                    val errorMessage = response.message ?: "Login failed"
                    _error.value = errorMessage
                }
            } catch (e: Exception) {
                val networkErrorMessage = "Network error occurred"
                _error.value = e.message ?: networkErrorMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveSession(user: UserModel) {
        EspressoIdlingResource.increment()
        viewModelScope.launch {
            repository.saveSession(user)
            EspressoIdlingResource.decrement()
        }
    }
}