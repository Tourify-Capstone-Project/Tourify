package com.capstone.project.tourify.ui.view.homepage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.FragmentHomePageBinding
import com.capstone.project.tourify.ui.adapter.ArticleAdapter
import com.capstone.project.tourify.ui.adapter.ArticleItem
import com.capstone.project.tourify.ui.adapter.CategoryHomeAdapter
import com.capstone.project.tourify.ui.adapter.CategoryItem
import com.capstone.project.tourify.ui.adapter.RecommendedAdapter
import com.capstone.project.tourify.ui.adapter.RecommendedItem
import com.capstone.project.tourify.ui.adapter.SettingAdapter
import com.capstone.project.tourify.ui.adapter.SettingItem

class HomePageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryHomeAdapter
    private lateinit var recommendedAdapter: RecommendedAdapter
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        val view = binding.root

        val categoryItems = listOf(
            CategoryItem("Bahari", R.drawable.bahari),
            CategoryItem("Village \nTourism", R.drawable.village_tourism),
            CategoryItem("Cagar \nAlam", R.drawable.cagar_alam),
            CategoryItem("Taman \nNasional", R.drawable.taman_nasional),
            CategoryItem("Culture", R.drawable.culture),
            CategoryItem("Culinary \nDestination", R.drawable.culinary)
        )

        val recommendedItems = listOf(
            RecommendedItem("The Great Asia Africa", R.drawable.no_image),
            RecommendedItem("The Great Asia Africano numero uno", R.drawable.no_image),
            RecommendedItem("The Great Asia Africa", R.drawable.no_image),
            RecommendedItem("The Great Asia Africa", R.drawable.no_image),
            RecommendedItem("The Great Asia Africa", R.drawable.no_image)
        )

        val articleItems = listOf(
            ArticleItem(
                "Artikel Abal-Abal Hanya Orang Kuat Iman Yang Dapat Membukanya",
                "Description artikel ini sangat membatu untuk anda yang sedang bermalas-malasan seperti saya",
                R.drawable.no_image
            ),
            ArticleItem("Title 2", "Description 2", R.drawable.no_image)
        )

        val settingCategoryHomeAdapter = CategoryHomeAdapter(categoryItems) { categoryItem ->
            handleCatergoryHomeAdapterItemClick(categoryItem)
        }

        categoryAdapter = settingCategoryHomeAdapter
        binding.rvCategory.adapter = categoryAdapter
        binding.rvCategory.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        recommendedAdapter = RecommendedAdapter(recommendedItems) { recommendedItems ->
            handleRecommendedItemClick(recommendedItems)
        }

        binding.rvRecommend.adapter = recommendedAdapter
        binding.rvRecommend.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        articleAdapter = ArticleAdapter(articleItems)
        binding.rvArticle.adapter = articleAdapter
        binding.rvArticle.layoutManager = LinearLayoutManager(context)
        return view
    }

    private fun handleCatergoryHomeAdapterItemClick(categoryItem: CategoryItem) {
        when (categoryItem.title) {
            "Bahari" -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_nav_home_to_categoryActivity)
            }

            "Village \nTourism" -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_nav_home_to_categoryActivity)
            }

            "Cagar \nAlam" -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_nav_home_to_categoryActivity)
            }
        }
    }

    private fun handleRecommendedItemClick(settingItem: RecommendedItem) {
        when (settingItem.title) {
            "The Great Asia Africa" -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_nav_home_to_detailActivity)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}