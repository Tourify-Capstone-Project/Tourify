package com.capstone.project.tourify.ui.adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.project.tourify.data.remote.response.DetailResponse
import com.capstone.project.tourify.ui.view.location.MapsFragment
import com.capstone.project.tourify.ui.view.overview.OverviewFragment
import com.capstone.project.tourify.ui.view.photogallery.PhotoGalleryFragment
import com.capstone.project.tourify.ui.view.review.ReviewFragment

class SectionPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private var detail: DetailResponse? = null

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OverviewFragment().apply {
                arguments = Bundle().apply {
                    val placeDesc = detail?.placeDesc
                    Log.d("SectionPagerAdapter", "placeDesc for OverviewFragment: $placeDesc")
                    putString("placeDesc", placeDesc)
                }
            }

            1 -> PhotoGalleryFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("additionalImages",
                        detail?.additionalImages?.let { ArrayList(it) })
                }
            }

            2 -> ReviewFragment()
            3 -> MapsFragment().apply {
                arguments = Bundle().apply {
                    putString("placeGmapsUrl", detail?.placeGmapsUrl)
                }
            }

            else -> throw IllegalStateException("Invalid position")
        }
    }

    fun updateDetail(detail: DetailResponse) {
        this.detail = detail
        notifyDataSetChanged()
    }
}