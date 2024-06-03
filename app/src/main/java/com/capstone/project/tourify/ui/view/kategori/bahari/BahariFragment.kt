package com.capstone.project.tourify.ui.view.kategori.bahari

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.FragmentBahariBinding
import com.capstone.project.tourify.ui.adapter.Category
import com.capstone.project.tourify.ui.adapter.CategoryAdapter

class BahariFragment : Fragment() {

    private var _binding: FragmentBahariBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBahariBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        adapter = CategoryAdapter(generateDummyCategories())
        binding.itemRowCategory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@BahariFragment.adapter
        }
    }

    private fun generateDummyCategories(): List<Category> {
        val categories = mutableListOf<Category>()
        categories.add(Category("Mount Rinjani", 4.5, "$10", R.drawable.rinjani))
        categories.add(Category("Ancol Park", 4.2, "$12", R.drawable.ancol))
        categories.add(Category("Pengandaran Beach", 3.8, "$15", R.drawable.pengandaran))
        categories.add(Category("Tangkuban Perahu", 4.0, "$11", R.drawable.tangkubangperahu))
        return categories
    }
}