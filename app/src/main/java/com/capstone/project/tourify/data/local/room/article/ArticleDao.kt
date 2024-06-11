package com.capstone.project.tourify.data.local.room.article

import androidx.paging.PagingSource
import androidx.room.*
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: List<ArticlesResponseItem>)

    @Query("SELECT * FROM article")
    fun getAllArticle(): PagingSource<Int, ArticlesResponseItem>

    @Query("DELETE FROM article")
    suspend fun deleteAll()
}