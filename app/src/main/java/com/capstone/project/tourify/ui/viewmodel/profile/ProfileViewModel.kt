package com.capstone.project.tourify.ui.viewmodel.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.project.tourify.data.local.entity.UserModel
import com.capstone.project.tourify.data.repository.AuthRepository

class ProfileViewModel(val repository: AuthRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}