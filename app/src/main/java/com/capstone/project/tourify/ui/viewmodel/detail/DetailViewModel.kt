package com.capstone.project.tourify.ui.viewmodel.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.project.tourify.data.remote.response.DetailResponse
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    private val _detail = MutableLiveData<DetailResponse?>()
    val detail: LiveData<DetailResponse?> get() = _detail

    fun getDetail(tourismId: String) {
        viewModelScope.launch {
            _detail.value = repository.getDetail(tourismId)
        }
    }
}
