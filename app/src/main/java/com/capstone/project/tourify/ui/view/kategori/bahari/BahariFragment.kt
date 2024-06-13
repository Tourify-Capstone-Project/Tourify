package com.capstone.project.tourify.ui.view.kategori.bahari

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.databinding.FragmentBahariBinding
import com.capstone.project.tourify.ui.adapter.CategoryAdapter
import com.capstone.project.tourify.ui.viewmodel.category.culinary.CulinaryViewModel
import com.capstone.project.tourify.ui.viewmodel.shared.SharedViewModel
import com.capstone.project.tourify.ui.viewmodelfactory.ViewModelFactory

class BahariFragment : Fragment() {

    private var _binding: FragmentBahariBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CategoryAdapter
    private val categoryViewModel: CulinaryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBahariBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        categoryViewModel.getCategoriesByType("ctgry0hdxzlz391ntutwchm7gfrtvptfry089")

        categoryViewModel.categories.observe(viewLifecycleOwner) { categories ->
            Log.d("BahariFragment", "Categories: ${categories.size}")
            adapter.updateCategories(categories)
        }

        categoryViewModel.filteredCategories.observe(viewLifecycleOwner) { filteredCategories ->
            Log.d("BahariFragment", "Filtered Categories: ${filteredCategories.size}")
            adapter.updateCategories(filteredCategories)
        }

        sharedViewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            Log.d("BahariFragment", "Search Query: $query")
            categoryViewModel.filterCategories(query)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        adapter = CategoryAdapter(emptyList())
        binding.itemRowCategory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@BahariFragment.adapter
        }
    }
}