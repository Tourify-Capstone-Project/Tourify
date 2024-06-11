package com.capstone.project.tourify.ui.view.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.FragmentHomePageBinding
import com.capstone.project.tourify.ui.adapter.ArticleAdapter
import com.capstone.project.tourify.ui.adapter.CategoryHomeAdapter
import com.capstone.project.tourify.ui.adapter.CategoryItem
import com.capstone.project.tourify.ui.adapter.LoadingStateAdapter
import com.capstone.project.tourify.ui.adapter.RecommendedAdapter
import com.capstone.project.tourify.ui.adapter.RecommendedItem
import com.capstone.project.tourify.ui.viewmodel.article.ArticleViewModel
import com.capstone.project.tourify.ui.viewmodelfactory.ViewModelFactory

class HomePageFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var categoryAdapter: CategoryHomeAdapter
    private lateinit var recommendedAdapter: RecommendedAdapter
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapterCategory()
        setupAdapterRecommended()
        setupRecyclerView()
        observeHeadlineNews()
    }

    private fun setupAdapterCategory() {
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

        categoryAdapter = CategoryHomeAdapter(categoryItems) { categoryItem ->
            handleCategoryItemClick(categoryItem)
        }

        binding.rvCategory.apply {
            adapter = categoryAdapter
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

        recommendedAdapter = RecommendedAdapter(recommendedItems) { recommendedItem ->
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

    private fun observeHeadlineNews() {
        viewModel.getHeadlineNews.observe(viewLifecycleOwner, { pagingData ->
            articleAdapter.submitData(lifecycle, pagingData)
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}