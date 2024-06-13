package com.capstone.project.tourify.ui.view.kategori.Culinary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.databinding.FragmentCagarBinding
import com.capstone.project.tourify.ui.adapter.CategoryAdapter
import com.capstone.project.tourify.ui.adapter.LoadingStateAdapter
import com.capstone.project.tourify.ui.viewmodel.category.culinary.CulinaryViewModel
import com.capstone.project.tourify.ui.viewmodel.shared.SharedViewModel
import com.capstone.project.tourify.ui.viewmodelfactory.ViewModelFactory

class CulinaryFragment : Fragment() {

    private var _binding: FragmentCagarBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryadapter: CategoryAdapter
    private val categoryViewModel: CulinaryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCagarBinding.inflate(inflater, container, false)
        return binding.root
    }
}

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        categoryadapter = CategoryAdapter()
//
//        setupRecyclerView()
//
//        categoryViewModel.getCategoriesByType("ctgry7hc1oq4or1ymwddw2bu8uan5ntfry089")
//<<<<<<< HEAD:app/src/main/java/com/capstone/project/tourify/ui/view/kategori/culinary/CulinaryFragment.kt
//            .observe(viewLifecycleOwner, Observer { pagingData ->
//                categoryadapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
//            })
//=======
//>>>>>>> e0f0ca43ca94223575f1eab8f391b15c5123c7a8:app/src/main/java/com/capstone/project/tourify/ui/view/kategori/Culinary/CulinaryFragment.kt
//
//        categoryViewModel.categories.observe(viewLifecycleOwner) { categories ->
//            adapter.updateCategories(categories)
//        }
//
//        categoryViewModel.filteredCategories.observe(viewLifecycleOwner) { filteredCategories ->
//            adapter.updateCategories(filteredCategories)
//        }
//
//        sharedViewModel.searchQuery.observe(viewLifecycleOwner) { query ->
//            categoryViewModel.filterCategories(query)
//        }
//    }
//
//    private fun setupRecyclerView() {
//        binding.itemRowCategory.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = this@CulinaryFragment.categoryadapter.withLoadStateFooter(
//                footer = LoadingStateAdapter { this@CulinaryFragment.categoryadapter.retry() }
//            )
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//<<<<<<< HEAD:app/src/main/java/com/capstone/project/tourify/ui/view/kategori/culinary/CulinaryFragment.kt
//=======
//
//    private fun setupRecyclerView() {
//        adapter = CategoryAdapter(emptyList())
//        binding.itemRowCategory.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = this@CulinaryFragment.adapter
//        }
//    }
//>>>>>>> e0f0ca43ca94223575f1eab8f391b15c5123c7a8:app/src/main/java/com/capstone/project/tourify/ui/view/kategori/Culinary/CulinaryFragment.kt
//}
