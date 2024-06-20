package com.capstone.project.tourify.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.project.tourify.data.remote.response.DetailResponse
import com.capstone.project.tourify.ui.view.overview.OverviewFragment
import com.capstone.project.tourify.ui.view.photogallery.PhotoGalleryFragment
import com.capstone.project.tourify.ui.view.review.ReviewFragment

class SectionPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private var detail: DetailResponse? = null

    override fun getItemCount(): Int = 3 // Update count to 3 since "Located" is not included

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OverviewFragment().apply {
                arguments = Bundle().apply {
                    putString("placeDesc", detail?.placeDesc)
                }
            }

            1 -> PhotoGalleryFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("additionalImages", detail?.additionalImages?.let { ArrayList(it) })
                }
            }

            2 -> ReviewFragment()

            else -> throw IllegalStateException("Invalid position")
        }
    }

    fun updateDetail(detail: DetailResponse) {
        this.detail = detail
        notifyDataSetChanged()
    }
}