package com.capstone.project.tourify.data.pagging

import androidx.datastore.core.IOException
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.remote.retrofit.ApiService
import retrofit2.HttpException

class ArticlePagingSource(
    private val apiService: ApiService
) : PagingSource<Int, ArticlesResponseItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticlesResponseItem> {
        val position = params.key ?: 1
        return try {
            val articles = apiService.getArticles(position, params.loadSize)
            val nextKey = if (articles.isEmpty()) null else position + 1
            LoadResult.Page(
                data = articles,
                prevKey = if (position == 1) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticlesResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}