package com.capstone.project.tourify.ui.view.kategori

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.ActivityKategoriBinding
import com.capstone.project.tourify.ui.adapter.Category
import com.capstone.project.tourify.ui.adapter.CategoryAdapter

@Suppress("DEPRECATION")
class KategoriActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKategoriBinding
    private lateinit var adapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        val dummyCategoryList = generateDummyCategories()
        adapter = CategoryAdapter(dummyCategoryList)
        binding.itemRowCategory.layoutManager = LinearLayoutManager(this)
        binding.itemRowCategory.adapter = adapter
    }

    private fun generateDummyCategories(): List<Category> {
        val categories = mutableListOf<Category>()
        categories.add(Category("Mount Rinjani", 4.5, "$10", R.drawable.rinjani))
        categories.add(Category("Ancol Park", 4.2, "$12", R.drawable.ancol))
        categories.add(Category("Pengandaran Beach", 3.8, "$15", R.drawable.pengandaran))
        categories.add(Category("Tangkuban Perahu", 4.0, "$11", R.drawable.tangkubangperahu))
        return categories
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.materialBarCategory)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            title = getString(R.string.string_kategori)
        }

        binding.materialBarCategory.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}