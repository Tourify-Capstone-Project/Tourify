package com.capstone.project.tourify.data.pagging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.capstone.project.tourify.data.local.room.article.ArticleDatabase
import com.capstone.project.tourify.data.remote.response.AllDestinationResponseItem
import com.capstone.project.tourify.data.remote.response.ArticlesResponse
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.data.remote.response.CategoryResponseItem
import com.capstone.project.tourify.data.remote.response.DetailResponse
import com.capstone.project.tourify.data.remote.response.DetailUserResponse
import com.capstone.project.tourify.data.remote.response.FavoriteResponse
import com.capstone.project.tourify.data.remote.response.FinanceResponse
import com.capstone.project.tourify.data.remote.response.PhotoProfileResponse
import com.capstone.project.tourify.data.remote.response.ReviewResponse
import com.capstone.project.tourify.data.remote.response.ReviewUserResponse
import com.capstone.project.tourify.data.remote.retrofit.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class ArticleRemoteMediatorTest{

    private var mockApi: ApiService = FakeApiService()
    private var mockDb: ArticleDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        ArticleDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = ArticleRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, ArticlesResponseItem>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService : ApiService {

    override suspend fun getArticles(page: Int, size: Int): List<ArticlesResponseItem> {
        val items: MutableList<ArticlesResponseItem> = arrayListOf()

        for (i in 0..100) {
            val article = ArticlesResponseItem(
                i.toString(),
                "Rekomendasi Kuliner Pontianak Paling Enak dan Legendaris",
                "Pontianak",
                "https://phinemo.com/wp-content/uploads/2017/12/23594516_1641361239236641_7260894878224089088_n.jpg",
                "Terkenal sebagai kota khatulistiwa, Pontianak seakan menjadi magnet tersendiri bagi wisatawan. Keragaman budaya Dayak, Melayu dan Tionghoa menambah keistimewaan Kota Khatulistiwa ini di mata wisatawan. Bahkan karena keragaman budaya itu,  kuliner Pontianak juga populer sangat variatif.",
                "https://phinemo.com/kuliner-pontianak-enak-dan-legendaris/"
            )
            items.add(article)
        }
        return items.subList((page - 1) * size, (page - 1) * size + size)
    }

    override suspend fun getCategory(
        category: String,
        page: Int,
        size: Int,
    ): List<CategoryResponseItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getDetail(tourismId: String): Response<DetailResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun addFavorite(tourismId: String): Response<DetailResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavorite(tourismId: String): Response<DetailResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteDestinations(): Response<FavoriteResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getFinanceDestinations(): Response<FinanceResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun searchDestinations(
        query: String,
        page: Int,
        size: Int,
    ): List<AllDestinationResponseItem> {
        TODO("Not yet implemented")
    }
}