package com.capstone.project.tourify.ui.viewmodel.article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.project.tourify.data.local.entity.category.CategoryEntity
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.repository.UserRepository
import kotlinx.coroutines.launch
import androidx.paging.filter
import com.capstone.project.tourify.data.local.entity.RecommendedItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

class ArticleViewModel(articlesRepository: UserRepository) : ViewModel() {

    val getHeadlineNews: LiveData<PagingData<ArticlesResponseItem>> =
        articlesRepository.getArticles().cachedIn(viewModelScope)

    private val _recommendations = MutableLiveData<List<RecommendedItem>?>()
    val recommendations: LiveData<List<RecommendedItem>?> get() = _recommendations

    // Fetch recommendations
    // Ambil rekomendasi
    fun fetchRecommendations() {
        viewModelScope.launch {
            val response = userRepository.getRecommendations()
            response?.let {
                val recommendedItems = it.detailsFavorite.map { item ->
                    RecommendedItem(
                        id = item.recomenId,
                        tourismId = item.tourismId,
                        name = item.detailPlace.placeName,
                        imageUrl = item.detailPlace.placePhotoUrl,
                        price = item.detailPlace.price,
                        rating = item.detailPlace.rating,
                        userId = item.userId
                    )
                }
                _recommendations.postValue(recommendedItems)
            }
        }
    }
}
