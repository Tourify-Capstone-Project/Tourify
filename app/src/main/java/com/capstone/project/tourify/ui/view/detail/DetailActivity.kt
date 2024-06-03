package com.capstone.project.tourify.ui.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.ActivityDetailBinding
import com.capstone.project.tourify.ui.adapter.SectionPagerAdapter
import com.capstone.project.tourify.ui.adapter.TabsAdapter

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var tabsAdapter: TabsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViewPager()
        setupRecyclerView()
    }
    private fun setupToolbar() {
        setSupportActionBar(binding.materialBarDetail)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            title = getString(R.string.appBar_Detail)
        }

        binding.materialBarDetail.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        val tabs = listOf("Description", "Photo Gallery", "Review", "Located")
        tabsAdapter = TabsAdapter(tabs) { position ->
            binding.viewPager.currentItem = position
        }

        binding.listItemTabs.apply {
            layoutManager =
                LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = tabsAdapter
        }
    }

    private fun setupViewPager() {
        val sectionsPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabsAdapter.setSelectedPosition(position)
            }
        })
    }
}