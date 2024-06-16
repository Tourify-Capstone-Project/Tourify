package com.capstone.project.tourify.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.project.tourify.data.repository.UserRepository
import com.capstone.project.tourify.di.Injection
import com.capstone.project.tourify.ui.viewmodel.article.ArticleViewModel
import com.capstone.project.tourify.ui.viewmodel.category.CategoryViewModel
import com.capstone.project.tourify.ui.viewmodel.category.culinary.CulinaryViewModel
import com.capstone.project.tourify.ui.viewmodel.detail.DetailViewModel
import com.capstone.project.tourify.ui.viewmodel.favorite.FavoriteViewModel
import com.capstone.project.tourify.ui.viewmodel.finance.FinanceViewModel

class ViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CategoryViewModel::class.java) -> {
                CategoryViewModel(repository) as T
            }
            
            modelClass.isAssignableFrom(ArticleViewModel::class.java) -> {
                ArticleViewModel(repository) as T
            }

            modelClass.isAssignableFrom(CulinaryViewModel::class.java) -> {
                CulinaryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FinanceViewModel::class.java) -> {
                FinanceViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @JvmStatic
        fun getInstance(context: Context) = ViewModelFactory(Injection.provideUserRepository(context))
    }
}