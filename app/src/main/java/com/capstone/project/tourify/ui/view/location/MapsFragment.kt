package com.capstone.project.tourify.ui.view.location

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.project.tourify.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private var placeGmapsUrl: String? = null

    private val callback = OnMapReadyCallback { googleMap ->
        placeGmapsUrl?.let {
            val location = getLocationFromGmapsUrl(it)
            googleMap.addMarker(MarkerOptions().position(location).title("Marker"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            placeGmapsUrl = it.getString("placeGmapsUrl")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun getLocationFromGmapsUrl(gmapsUrl: String): LatLng {
        val uri = android.net.Uri.parse(gmapsUrl)
        val latLng = uri.getQueryParameter("q")?.split(",")
        val lat = latLng?.get(0)?.toDoubleOrNull() ?: 0.0
        val lng = latLng?.get(1)?.toDoubleOrNull() ?: 0.0
        return LatLng(lat, lng)
    }
}
