package com.capstone.project.tourify.ui.viewmodel.shared

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.project.tourify.data.local.entity.RecommendedItem


class SharedViewModel : ViewModel() {

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    private val _tourismId = MutableLiveData<String>()
    val tourismId: LiveData<String> get() = _tourismId

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
