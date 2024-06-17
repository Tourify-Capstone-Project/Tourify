package com.capstone.project.tourify.ui.view.kategori

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.ActivityKategoriBinding
import com.capstone.project.tourify.ui.adapter.SectionPagerKategoriAdapter
import com.capstone.project.tourify.ui.adapter.TabsAdapter
import com.capstone.project.tourify.ui.viewmodel.shared.SharedViewModel

class KategoriActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKategoriBinding
    private lateinit var tabsAdapter: TabsAdapter
    private val sharedViewModel: SharedViewModel by viewModels()

    private val tabs = mutableListOf(
        "Bahari", "Village \nTourism", "Cagar \nAlam", "Taman \nNasional", "Culture", "Culinary \nDestination"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupViewPager()

        val categoryTitle = intent.getStringExtra("CATEGORY_TITLE")
        categoryTitle?.let { title ->
            val position = tabs.indexOfFirst { it.equals(title, ignoreCase = true) }
            if (position != -1) {
                binding.viewPager.currentItem = position
                tabsAdapter.setSelectedPosition(position)
            }
        }

        binding.listSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    sharedViewModel.setSearchQuery(it)
                }
                return true
            }
        })
    }

    private fun setupViewPager() {
        val sectionsPagerKategoriAdapter = SectionPagerKategoriAdapter(this)
        binding.viewPager.adapter = sectionsPagerKategoriAdapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabsAdapter.setSelectedPosition(position)
            }
        })
    }

    private fun setupRecyclerView() {
        tabsAdapter = TabsAdapter(tabs) { position ->
            binding.viewPager.currentItem = position
        }

        binding.listItemTabs.apply {
            layoutManager = LinearLayoutManager(this@KategoriActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = tabsAdapter
        }
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