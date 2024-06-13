package com.capstone.project.tourify.ui.view.kategori.culture

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.databinding.FragmentCagarBinding
import com.capstone.project.tourify.databinding.FragmentCultureBinding
import com.capstone.project.tourify.ui.adapter.CategoryAdapter
import com.capstone.project.tourify.ui.adapter.LoadingStateAdapter
import com.capstone.project.tourify.ui.viewmodel.category.culinary.CulinaryViewModel
import com.capstone.project.tourify.ui.viewmodelfactory.ViewModelFactory


class CultureFragment : Fragment() {

    private var _binding: FragmentCagarBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryadapter: CategoryAdapter
    private val categoryViewModel: CulinaryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCagarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryadapter = CategoryAdapter()

        setupRecyclerView()

        categoryViewModel.getCategoriesByType("ctgry2l00j6i8btbjfsq5l2wt1dn2utfry089")
            .observe(viewLifecycleOwner, Observer { pagingData ->
                categoryadapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            })

        categoryViewModel.refreshCategories("ctgry2l00j6i8btbjfsq5l2wt1dn2utfry089")
    }

    private fun setupRecyclerView() {
        binding.itemRowCategory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@CultureFragment.categoryadapter.withLoadStateFooter(
                footer = LoadingStateAdapter { this@CultureFragment.categoryadapter.retry() }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
