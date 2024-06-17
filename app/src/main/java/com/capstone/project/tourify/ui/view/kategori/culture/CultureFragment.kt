package com.capstone.project.tourify.ui.view.kategori.culture

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.databinding.FragmentCagarBinding
import com.capstone.project.tourify.databinding.FragmentCultureBinding
import com.capstone.project.tourify.ui.adapter.CategoryAdapter
import com.capstone.project.tourify.ui.adapter.LoadingStateAdapter
import com.capstone.project.tourify.ui.viewmodel.category.culinary.CulinaryViewModel
import com.capstone.project.tourify.ui.viewmodel.shared.SharedViewModel
import com.capstone.project.tourify.ui.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CultureFragment : Fragment() {

    private var _binding: FragmentCagarBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private val categoryViewModel: CulinaryViewModel by activityViewModels {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryAdapter = CategoryAdapter()

        setupRecyclerView()

        observeSearchQuery()
        loadInitialData()
        handleLoadState()
    }

    private fun setupRecyclerView() {
        binding.itemRowCategory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = categoryAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeSearchQuery() {
        sharedViewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            lifecycleScope.launch {
                if (query.isNotBlank()) {
                    categoryViewModel.filterCategories(
                        query,
                        "ctgry2l00j6i8btbjfsq5l2wt1dn2utfry089"
                    ).collectLatest { pagingData ->
                        categoryAdapter.submitData(pagingData)
                    }
                } else {
                    categoryAdapter.submitData(PagingData.empty()) // Clear current data
                    loadInitialData()
                }
            }
        }
    }

    private fun loadInitialData() {
        lifecycleScope.launch {
            categoryViewModel.getCategoriesByType("ctgry2l00j6i8btbjfsq5l2wt1dn2utfry089")
                .collectLatest { pagingData ->
                    categoryAdapter.submitData(pagingData)
                }
        }
    }

    private fun handleLoadState() {
        lifecycleScope.launch {
            categoryAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.progressIndicator.visibility =
                    if (loadStates.refresh is LoadState.Loading) View.VISIBLE else View.GONE

                val isEmpty =
                    loadStates.refresh is LoadState.NotLoading && categoryAdapter.itemCount == 0
                binding.tvLocationNotFound.visibility = if (isEmpty) View.VISIBLE else View.GONE
                binding.itemRowCategory.visibility = if (isEmpty) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}