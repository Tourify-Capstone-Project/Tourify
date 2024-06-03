package com.capstone.project.tourify.ui.view.kategori

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.ActivityKategoriBinding
import com.capstone.project.tourify.ui.adapter.CategoryAdapter
import com.capstone.project.tourify.ui.adapter.SectionPagerAdapter
import com.capstone.project.tourify.ui.adapter.SectionPagerKategoriAdapter
import com.capstone.project.tourify.ui.adapter.TabsAdapter

@Suppress("DEPRECATION")
class KategoriActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKategoriBinding

    private lateinit var tabsAdapter: TabsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViewPager()
        setupRecyclerView()

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
        val tabs = listOf("Bahari", "Village \nTourism", "Cagar \nAlam", "Taman \nNation", "Culture", "Culinary \nDestination")
        tabsAdapter = TabsAdapter(tabs) { position ->
            binding.viewPager.currentItem = position
        }

        binding.listItemTabs.apply {
            layoutManager =
                LinearLayoutManager(this@KategoriActivity, LinearLayoutManager.HORIZONTAL, false)
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