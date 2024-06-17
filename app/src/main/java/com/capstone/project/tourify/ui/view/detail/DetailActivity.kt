package com.capstone.project.tourify.ui.view.detail

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.capstone.project.tourify.R
import com.capstone.project.tourify.data.local.entity.favorite.FavoriteEntity
import com.capstone.project.tourify.data.local.room.favorite.FavoriteDatabase
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.data.remote.response.DetailResponse
import com.capstone.project.tourify.databinding.ActivityDetailBinding
import com.capstone.project.tourify.ui.adapter.SectionPagerAdapter
import com.capstone.project.tourify.ui.adapter.TabsAdapter
import com.capstone.project.tourify.ui.viewmodelfactory.ViewModelFactory
import com.capstone.project.tourify.ui.viewmodel.detail.DetailViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var tabsAdapter: TabsAdapter
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var sectionsPagerAdapter: SectionPagerAdapter

    private var isFavorite = false
    private lateinit var favoriteDatabase: FavoriteDatabase
    private lateinit var authToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteDatabase = FavoriteDatabase.getDatabase(this)

        val userPreference = UserPreference.getInstance(this)
        authToken = runBlocking { userPreference.getSession().first().token }

        setupToolbar()
        setupRecyclerView()
        setupFavoriteButton()

        val tourismId = intent.getStringExtra("tourism_id") ?: return

        detailViewModel.getDetail(tourismId)
        detailViewModel.detail.observe(this) { detail ->
            detail?.let {
                Log.d("DetailActivity", "Detail Response: $detail")
                updateUI(it)
                setupViewPager(it)
                checkFavoriteStatus(tourismId)
            }
        }
    }

    private fun updateUI(detail: DetailResponse) {
        Glide.with(this).load(detail.placePhotoUrl).into(binding.detailImageMain)
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
        binding.materialBarDetail.setNavigationOnClickListener { onBackPressed() }
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

    private fun setupViewPager(detail: DetailResponse) {
        sectionsPagerAdapter = SectionPagerAdapter(this).apply {
            updateDetail(detail)
        }
        binding.viewPager.adapter = sectionsPagerAdapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabsAdapter.setSelectedPosition(position)
            }
        })
    }

    private fun setupFavoriteButton() {
        val fabFavorite: FloatingActionButton = findViewById(R.id.fab_favorite)
        fabFavorite.setOnClickListener {
            val tourismId = intent.getStringExtra("tourism_id") ?: return@setOnClickListener
            if (isFavorite) {
                removeFavorite(tourismId)
            } else {
                addFavorite(tourismId)
            }
        }
    }

//    private fun showFavoriteConfirmationDialog(tourismId: String) {
//        AlertDialog.Builder(this)
//            .setMessage("Do you want to add this place to your favorites?")
//            .setPositiveButton("Yes") { dialog, _ ->
//                addFavorite(tourismId)
//                dialog.dismiss()
//            }
//            .setNegativeButton("No") { dialog, _ ->
//                dialog.dismiss()
//            }
//            .create()
//            .apply {
//                setOnShowListener {
//                    getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
//                    getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
//                }
//            }
//            .show()
//    }
//
//    private fun showUnfavoriteConfirmationDialog(tourismId: String) {
//        AlertDialog.Builder(this)
//            .setMessage("Are you sure you want to remove this place from your favorites?")
//            .setPositiveButton("Yes") { dialog, _ ->
//                removeFavorite(tourismId)
//                dialog.dismiss()
//            }
//            .setNegativeButton("No") { dialog, _ ->
//                dialog.dismiss()
//            }
//            .create()
//            .apply {
//                setOnShowListener {
//                    getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
//                    getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
//                }
//            }
//            .show()
//    }

    private fun checkFavoriteStatus(tourismId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val favorite = detailViewModel.getFavoriteById(tourismId)
            isFavorite = favorite != null
            runOnUiThread { updateFavoriteIcon() }
        }
    }

    private fun updateFavoriteIcon() {
        val fabFavorite: FloatingActionButton = findViewById(R.id.fab_favorite)
        fabFavorite.setImageResource(if (isFavorite) R.drawable.favorite else R.drawable.favorite_border)
    }


    private fun addFavorite(tourismId: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val detail = detailViewModel.detail.value ?: return@launch
            val favorite = FavoriteEntity(
                id = tourismId,
                name = detail.placeName,
                imageUrl = detail.placePhotoUrl,
                price = detail.price,
                userId = detail.city,
                tourismId = detail.placeId
            )
            detailViewModel.addFavorite(favorite)
            val response = detailViewModel.addFavoriteToRemote(tourismId)
            if (response.isSuccessful) {
                isFavorite = true
                runOnUiThread { updateFavoriteIcon() }
            } else {
                Log.e("DetailActivity", "Failed to add favorite: ${response.code()} - ${response.errorBody()?.string()}")
            }
        }
    }

    private fun removeFavorite(tourismId: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            detailViewModel.removeFavoriteById(tourismId)
            val response = detailViewModel.removeFavoriteFromRemote(tourismId)
            if (response.isSuccessful) {
                isFavorite = false
                runOnUiThread { updateFavoriteIcon() }
            } else {
                Log.e("DetailActivity", "Failed to remove favorite: ${response.code()} - ${response.errorBody()?.string()}")
            }
        }
    }
}
