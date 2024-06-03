package com.capstone.project.tourify.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.project.tourify.ui.view.location.MapsFragment
import com.capstone.project.tourify.ui.view.overview.OverviewFragment
import com.capstone.project.tourify.ui.view.photogallery.PhotoGalleryFragment
import com.capstone.project.tourify.ui.view.review.ReviewFragment

class SectionPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OverviewFragment()
            1 -> PhotoGalleryFragment()
            2 -> ReviewFragment()
            3 -> MapsFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}
