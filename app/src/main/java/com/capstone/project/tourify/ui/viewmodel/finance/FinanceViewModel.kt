package com.capstone.project.tourify.ui.viewmodel.finance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.project.tourify.data.remote.response.FinanceResponse
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.launch

class FinanceViewModel(private val repository: UserRepository) : ViewModel() {

    private val _financeData = MutableLiveData<FinanceResponse?>()
    val financeData: LiveData<FinanceResponse?> get() = _financeData

    fun loadFinanceData() {
        viewModelScope.launch {
            val response = repository.getFinanceDestinations()
            _financeData.postValue(response)
        }
    }
}
