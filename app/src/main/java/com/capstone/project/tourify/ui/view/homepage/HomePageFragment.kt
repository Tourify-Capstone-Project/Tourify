package com.capstone.project.tourify.ui.view.homepage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.R
import com.capstone.project.tourify.data.local.entity.RecommendedItem
import com.capstone.project.tourify.databinding.FragmentHomePageBinding
import com.capstone.project.tourify.ui.adapter.*
import com.capstone.project.tourify.ui.view.detail.DetailActivity
import com.capstone.project.tourify.ui.viewmodelfactory.ViewModelFactory
import com.capstone.project.tourify.ui.viewmodel.article.ArticleViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomePageFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryHomeAdapter: CategoryHomeAdapter
    private lateinit var recommendedAdapter: RecommendedAdapter
    private lateinit var articleAdapter: ArticleAdapter

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        val view = binding.root

        setupAdapterRecommended()

        // Observe rekomendasi
        viewModel.recommendations.observe(viewLifecycleOwner) { response ->
            response?.let {
                Log.d("HomePageFragment", "Rekomendasi berhasil dimuat: $it")
                recommendedAdapter.setItems(it)
            } ?: run {
                Log.e("HomePageFragment", "Gagal memuat rekomendasi")
                Toast.makeText(context, "Gagal memuat rekomendasi", Toast.LENGTH_SHORT).show()
            }
        }

        // Fetch rekomendasi
        lifecycleScope.launch {
            viewModel.fetchRecommendations()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapterHomeCategory()
        setupRecyclerView()
        setupAdapterCategory()
        observeHeadlineNews()
        setupSearchView()
    }

    private fun setupAdapterHomeCategory() {
        val categoryItems = listOf(
            CategoryItem("Bahari", R.drawable.bahari, "ctgry0hdxzlz391ntutwchm7gfrtvptfry089"),
            CategoryItem("Wisata \nDesa", R.drawable.village_tourism, "ctgryeu9qus02crsy52mxao1xqciihtfry089"),
            CategoryItem("Cagar \nAlam", R.drawable.cagar_alam, "ctgryla6bw54fikev61qdftdgpxbkctfry089"),
            CategoryItem("Taman \nNasional", R.drawable.taman_nasional, "ctgryeb3hb4el990rapy8v7x0ia84gtfry089"),
            CategoryItem("Budaya", R.drawable.culture, "ctgry2l00j6i8btbjfsq5l2wt1dn2utfry089"),
            CategoryItem("Destinasi \nKuliner", R.drawable.culinary, "ctgry7hc1oq4or1ymwddw2bu8uan5ntfry089")
        )

        categoryHomeAdapter = CategoryHomeAdapter(categoryItems) { categoryItem ->
            handleCategoryItemClick(categoryItem)
        }

        binding.rvCategory.apply {
            adapter = categoryHomeAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupAdapterRecommended() {
        recommendedAdapter = RecommendedAdapter { recommendedItem ->
            handleRecommendedItemClick(recommendedItem)
        }

        binding.rvRecommend.apply {
            adapter = recommendedAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticleAdapter()
        binding.rvArticle.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = articleAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    articleAdapter.retry()
                }
            )
            setHasFixedSize(true)
        }

        articleAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                showLoading(true)
            } else {
                showLoading(false)

                val errorState = loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    showErrorMessage(true, it.error.localizedMessage ?: "Error")
                }
            }
        }
        binding.rvArticle.itemAnimator = null // Nonaktifkan animator item untuk menghindari masalah ketidakonsistenan
    }

    private fun setupSearchView() {
        binding.listSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    performSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    searchJob?.cancel()
                    searchJob = lifecycleScope.launch {
                        delay(1000) // waktu tunda debounce
                        performSearch(newText)
                    }
                } else {
                    resetUI()
                }
                return true
            }
        })

        binding.listSearch.setOnCloseListener {
            resetUI()
            true
        }
    }

    private fun performSearch(query: String) {
        searchJob?.cancel() // Batalkan pencarian yang sedang berlangsung
        searchJob = lifecycleScope.launch {
            viewModel.searchDestinations(query)
            // Notifikasi adapter bahwa data telah berubah
            categoryAdapter.notifyDataSetChanged()
        }
        showSearchResultsUI(true)
    }

    private fun observeHeadlineNews() {
        viewModel.getHeadlineNews.observe(viewLifecycleOwner) { pagingData ->
            articleAdapter.submitData(lifecycle, pagingData)
        }

        lifecycleScope.launch {
            viewModel.isSearching.collect { isSearching ->
                if (isSearching) {
                    showSearchResultsUI(true)
                } else {
                    showSearchResultsUI(false) // Tampilkan UI asli saat pencarian tidak aktif
                }
            }
        }

        lifecycleScope.launch {
            viewModel.searchResults.collect { results ->
                categoryAdapter.submitData(lifecycle, results)
            }
        }
    }

    private fun showSearchResultsUI(show: Boolean) {
        if (show) {
            binding.rvSearchResults.visibility = View.VISIBLE
            binding.rvCategory.visibility = View.GONE
            binding.rvRecommend.visibility = View.GONE
            binding.rvArticle.visibility = View.GONE
            binding.titleCategory.visibility = View.GONE
            binding.titleRecommend.visibility = View.GONE
            binding.titleArticle.visibility = View.GONE
        } else {
            resetUI()
        }
    }

    private fun resetUI() {
        binding.rvSearchResults.visibility = View.GONE
        binding.rvCategory.visibility = View.VISIBLE
        binding.rvRecommend.visibility = View.VISIBLE
        binding.rvArticle.visibility = View.VISIBLE
        binding.titleCategory.visibility = View.VISIBLE
        binding.titleRecommend.visibility = View.VISIBLE
        binding.titleArticle.visibility = View.VISIBLE
    }

    private fun showLoading(isLoading: Boolean) {
        _binding?.let {
            it.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showErrorMessage(isError: Boolean, message: String) {
        if (isError) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleRecommendedItemClick(recommendedItem: RecommendedItem) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra("tourism_id", recommendedItem.tourismId)
        }
        startActivity(intent)
    }

    private fun handleCategoryItemClick(settingItem: CategoryItem) {
        val bundle = Bundle().apply {
            putString("CATEGORY_TITLE", settingItem.title)
            putString("CATEGORY_ID", settingItem.id)
        }
        Navigation.findNavController(requireView())
            .navigate(R.id.action_nav_home_to_kategoriActivity, bundle)
    }

    private fun setupAdapterCategory() {
        categoryAdapter = CategoryAdapter()
        binding.rvSearchResults.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
