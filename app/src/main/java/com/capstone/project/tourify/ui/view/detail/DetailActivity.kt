package com.capstone.project.tourify.ui.view.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.capstone.project.tourify.R
import com.capstone.project.tourify.data.remote.response.DetailResponse
import com.capstone.project.tourify.databinding.ActivityDetailBinding
import com.capstone.project.tourify.ui.adapter.SectionPagerAdapter
import com.capstone.project.tourify.ui.adapter.TabsAdapter
import com.capstone.project.tourify.ui.viewmodelfactory.ViewModelFactory
import com.capstone.project.tourify.ui.viewmodel.detail.DetailViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var tabsAdapter: TabsAdapter
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var sectionsPagerAdapter: SectionPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViewPager()
        setupRecyclerView()

        val tourismId = intent.getStringExtra("tourism_id") ?: return

        detailViewModel.getDetail(tourismId)
        detailViewModel.detail.observe(this, Observer { detail ->
            detail?.let {
                updateUI(it)
                sectionsPagerAdapter.updateDetail(it)
            }
        })
    }

    private fun updateUI(detail: DetailResponse) {
        Glide.with(this)
            .load(detail.placePhotoUrl)
            .into(binding.detailImageMain)

        binding.titleDetail.text = detail.placeName
        binding.priceDetail.text = detail.price
        binding.rating.text = detail.rating
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
        sectionsPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabsAdapter.setSelectedPosition(position)
            }
        })
    }
}
