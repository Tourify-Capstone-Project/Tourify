package com.capstone.project.tourify.ui.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.capstone.project.tourify.R
import com.capstone.project.tourify.ui.view.location.MapsFragment
import com.capstone.project.tourify.ui.view.overview.OverviewFragment
import com.capstone.project.tourify.ui.view.review.ReviewFragment

class SectionPagerAdapter(
    private val context: Context,
    private val manager: FragmentManager
) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val TAB_TITLES =
        intArrayOf(R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3)

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OverviewFragment()
            1 -> ReviewFragment()
            2 -> MapsFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }
}
