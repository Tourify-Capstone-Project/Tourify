@file:Suppress("DEPRECATION")

package com.capstone.project.tourify.ui.view.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.ActivityDetailBinding
import com.capstone.project.tourify.ui.adapter.ImageDetailAdapter
import com.capstone.project.tourify.ui.adapter.SectionPagerAdapter
import com.google.android.material.tabs.TabLayout

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionPagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)

        val imageList = listOf(
            R.drawable.pengandaran,
            R.drawable.rinjani,
            R.drawable.tangkubangperahu,
            R.drawable.ancol,
            R.drawable.rinjani
        )

        val adapter = ImageDetailAdapter(this, imageList)
        binding.listItemImageDetail.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.listItemImageDetail.adapter = adapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.nestedScrollView.post {
                    binding.nestedScrollView.fullScroll(View.FOCUS_UP)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Tidak perlu melakukan apa-apa
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                binding.nestedScrollView.post {
                    binding.nestedScrollView.fullScroll(View.FOCUS_UP)
                }
            }
        })

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

}