package com.capstone.project.tourify.ui.viewmodel.article

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.capstone.project.tourify.DataDummy
import com.capstone.project.tourify.MainDispatcherRule
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.repository.UserRepository
import com.capstone.project.tourify.getOrAwaitValue
import com.capstone.project.tourify.ui.adapter.ArticleAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ArticleViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository

    @Test
    fun `when Get Article Should Not Null and Return Data`() = runTest {
        val dummyArticle = DataDummy.generateDummyArticleResponse()
        val data: PagingData<ArticlesResponseItem> = ArticlePagingSource.snapshot(dummyArticle)
        val expectedArticle = MutableLiveData<PagingData<ArticlesResponseItem>>()
        expectedArticle.value = data
        Mockito.`when`(userRepository.getArticles()).thenReturn(expectedArticle)

        val mainViewModel = ArticleViewModel(userRepository)
        val actualArticle: PagingData<ArticlesResponseItem> = mainViewModel.getHeadlineNews.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ArticleAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualArticle)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyArticle.size, differ.snapshot().size)
        Assert.assertEquals(dummyArticle[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Article Empty Should Return No Data`() = runTest {
        val data: PagingData<ArticlesResponseItem> = PagingData.from(emptyList())
        val expectedArticle = MutableLiveData<PagingData<ArticlesResponseItem>>()
        expectedArticle.value = data
        Mockito.`when`(userRepository.getArticles()).thenReturn(expectedArticle)
        val mainViewModel = ArticleViewModel(userRepository)
        val actualArticle: PagingData<ArticlesResponseItem> = mainViewModel.getHeadlineNews.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = ArticleAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualArticle)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class ArticlePagingSource : PagingSource<Int, LiveData<List<ArticlesResponseItem>>>() {
    companion object {
        fun snapshot(items: List<ArticlesResponseItem>): PagingData<ArticlesResponseItem> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ArticlesResponseItem>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ArticlesResponseItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}