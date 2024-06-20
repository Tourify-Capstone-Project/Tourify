package com.capstone.project.tourify.ui.view.homepage

import android.os.Bundle
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
import com.capstone.project.tourify.data.local.entity.category.CategoryEntity
import com.capstone.project.tourify.databinding.FragmentHomePageBinding
import com.capstone.project.tourify.ui.adapter.ArticleAdapter
import com.capstone.project.tourify.ui.adapter.CategoryAdapter
import com.capstone.project.tourify.ui.adapter.CategoryHomeAdapter
import com.capstone.project.tourify.ui.adapter.CategoryItem
import com.capstone.project.tourify.ui.adapter.LoadingStateAdapter
import com.capstone.project.tourify.ui.adapter.RecommendedAdapter
import com.capstone.project.tourify.ui.adapter.RecommendedItem
import com.capstone.project.tourify.ui.viewmodel.article.ArticleViewModel
import com.capstone.project.tourify.ui.viewmodel.article.HomeViewModel
import com.capstone.project.tourify.ui.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class HomePageFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var tflite: Interpreter
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

        tflite = Interpreter(loadModelFile())

        return binding.root
    }

    private fun loadModelFile(): ByteBuffer {
        val fileDescriptor = requireContext().assets.openFd("tourify_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength).apply {
            order(ByteOrder.nativeOrder())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapterHomeCategory()
        setupAdapterRecommended()
        setupRecyclerView()
        observeHeadlineNews()
        setupSearchView()
        setupAdapterCategory()
    }

    private fun setupAdapterHomeCategory() {
        val categoryItems = listOf(
            CategoryItem("Bahari", R.drawable.bahari, "ctgry0hdxzlz391ntutwchm7gfrtvptfry089"),
            CategoryItem(
                "Village \nTourism",
                R.drawable.village_tourism,
                "ctgryeu9qus02crsy52mxao1xqciihtfry089"
            ),
            CategoryItem(
                "Cagar \nAlam",
                R.drawable.cagar_alam,
                "ctgryla6bw54fikev61qdftdgpxbkctfry089"
            ),
            CategoryItem(
                "Taman \nNasional",
                R.drawable.taman_nasional,
                "ctgryeb3hb4el990rapy8v7x0ia84gtfry089"
            ),
            CategoryItem("Culture", R.drawable.culture, "ctgry2l00j6i8btbjfsq5l2wt1dn2utfry089"),
            CategoryItem(
                "Culinary \nDestination",
                R.drawable.culinary,
                "ctgry7hc1oq4or1ymwddw2bu8uan5ntfry089"
            )
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
        val recommendedItems = listOf(
            RecommendedItem("The Great Asia Africa", R.drawable.no_image),
            RecommendedItem("The Great Asia Africano numero uno", R.drawable.no_image),
            RecommendedItem("The Great Asia Africa", R.drawable.no_image),
            RecommendedItem("The Great Asia Africa", R.drawable.no_image),
            RecommendedItem("The Great Asia Africa", R.drawable.no_image)
        )

        recommendedAdapter = RecommendedAdapter(recommendedItems, tflite) { recommendedItem ->
            handleRecommendedItemClick(recommendedItem)
        }

        binding.rvRecommend.adapter = recommendedAdapter
        binding.rvRecommend.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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
    }

    private fun setupSearchView() {
        binding.listSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    performSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.isNotEmpty()) {
                    searchJob?.cancel()
                    searchJob = lifecycleScope.launch {
                        delay(1000) // debounce timeOut
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
        searchJob?.cancel() // Cancel any ongoing search
        searchJob = lifecycleScope.launch {
            homeViewModel.searchDestinations(query)
            showSearchResultsUI(true)
        }
    }

    private fun observeHeadlineNews() {
        viewModel.getHeadlineNews.observe(viewLifecycleOwner) { pagingData ->
            articleAdapter.submitData(lifecycle, pagingData)
        }

        homeViewModel.isSearching.observe(viewLifecycleOwner) { isSearching ->
            if (isSearching) {
                showSearchResultsUI(true)
            } else {
                showSearchResultsUI(true)
            }
        }

        homeViewModel.searchResults.observe(viewLifecycleOwner) { results ->
            if (results.isNotEmpty()) {
                showSearchResults(results)
            } else {
                showSearchResultsUI(false)
            }
        }
    }

    private fun showSearchResults(results: List<CategoryEntity>) {
        categoryAdapter.updateCategories(results)
        binding.rvSearchResults.adapter = categoryAdapter
        binding.rvSearchResults.visibility = View.VISIBLE
        binding.rvCategory.visibility = View.GONE
        binding.rvRecommend.visibility = View.GONE
        binding.rvArticle.visibility = View.GONE
        binding.titleCategory.visibility = View.GONE
        binding.titleRecommend.visibility = View.GONE
        binding.titleArticle.visibility = View.GONE
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
        when (recommendedItem.title) {
            "The Great Asia Africa" -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_nav_home_to_detailActivity)
            }
        }
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
        categoryAdapter = CategoryAdapter(emptyList())
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