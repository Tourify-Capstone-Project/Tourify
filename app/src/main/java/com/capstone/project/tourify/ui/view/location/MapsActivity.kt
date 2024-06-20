package com.capstone.project.tourify.ui.view.location

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.capstone.project.tourify.R
import com.capstone.project.tourify.data.remote.response.DetailResponse

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.capstone.project.tourify.databinding.ActivityMapsBinding
import com.capstone.project.tourify.ui.viewmodel.detail.DetailViewModel
import com.capstone.project.tourify.ui.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val locationViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tourismId = intent.getStringExtra("tourism_id") ?: return

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationViewModel.getDetail(tourismId)

        lifecycleScope.launch {
            locationViewModel.detail.observe(this@MapsActivity) { detail ->
                detail?.let {
                    updateMapLocation(it)
                    setupToolbar(it.placeName)
                }
            }
        }

        binding.fabGmaps.setOnClickListener {
            val detail = locationViewModel.detail.value ?: return@setOnClickListener
            openPlaceUrl(detail.placeGmapsUrl)
        }
    }

    private fun updateMapLocation(detailResponse: DetailResponse) {
        val latitude = detailResponse.latitude.toDoubleOrNull() ?: return
        val longitude = detailResponse.longitude.toDoubleOrNull() ?: return
        val location = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(location).title(detailResponse.placeName))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }

    private fun setupToolbar(placeTitle: String) {
        setSupportActionBar(binding.materialBarDetail)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            title = placeTitle
        }
        binding.materialBarDetail.setNavigationOnClickListener { onBackPressed() }
    }

    // Function to open place URL in web browser
    private fun openPlaceUrl(placeUrl: String) {
        val uri = Uri.parse(placeUrl)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}