package com.capstone.project.tourify.ui.view.homepage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.FragmentHomePageBinding
import com.capstone.project.tourify.ui.adapter.ArticleAdapter
import com.capstone.project.tourify.ui.adapter.ArticleItem
import com.capstone.project.tourify.ui.adapter.CategoryHomeAdapter
import com.capstone.project.tourify.ui.adapter.CategoryItem
import com.capstone.project.tourify.ui.adapter.RecommendedAdapter
import com.capstone.project.tourify.ui.adapter.RecommendedItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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

        // Contoh data
        val categoryItems = listOf(
            CategoryItem("Bahari", R.drawable.bahari),
            CategoryItem("Village \nTourism", R.drawable.village_tourism),
            CategoryItem("Cagar \nAlam", R.drawable.cagar_alam),
            CategoryItem("Taman \nNasional", R.drawable.taman_nasional),
            CategoryItem("Culture", R.drawable.culture),
            CategoryItem("Culinary \nDestination", R.drawable.culinary)
        )

        val recommendedItems = listOf(
            RecommendedItem("The Great Asia Africa", R.drawable.coba),
            RecommendedItem("The Great Asia Africano numero uno", R.drawable.coba),
            RecommendedItem("The Great Asia Africa", R.drawable.coba),
            RecommendedItem("The Great Asia Africa", R.drawable.coba),
            RecommendedItem("The Great Asia Africa", R.drawable.coba)
        )

        val articleItems = listOf(
            ArticleItem("Artikel Abal-Abal Hanya Orang Kuat Iman Yang Dapat Membukanya", "Description artikel ini sangat membatu untuk anda yang sedang bermalas-malasan seperti saya", R.drawable.coba),
            ArticleItem("Title 2", "Description 2", R.drawable.coba)
            // Tambahkan artikel lainnya di sini
        )

        // Inisialisasi Adapter dan set ke RecyclerView untuk kategori
        categoryAdapter = CategoryHomeAdapter(categoryItems)
        binding.rvCategory.adapter = categoryAdapter
        binding.rvCategory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Inisialisasi Adapter dan set ke RecyclerView untuk rekomendasi
        recommendedAdapter = RecommendedAdapter(recommendedItems)
        binding.rvRecommend.adapter = recommendedAdapter
        binding.rvRecommend.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Inisialisasi Adapter dan set ke RecyclerView untuk artikel
        articleAdapter = ArticleAdapter(articleItems)
        binding.rvArticle.adapter = articleAdapter
        binding.rvArticle.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomePageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}