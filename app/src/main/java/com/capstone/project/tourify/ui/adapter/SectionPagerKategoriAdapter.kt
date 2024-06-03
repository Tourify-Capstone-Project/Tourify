package com.capstone.project.tourify.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.project.tourify.ui.view.kategori.Culinary.CulinaryFragment
import com.capstone.project.tourify.ui.view.kategori.bahari.BahariFragment
import com.capstone.project.tourify.ui.view.kategori.cagar.CagarFragment
import com.capstone.project.tourify.ui.view.kategori.culture.CultureFragment
import com.capstone.project.tourify.ui.view.kategori.taman.TamanFragment
import com.capstone.project.tourify.ui.view.kategori.village.VillageFragment

class SectionPagerKategoriAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BahariFragment()
            1 -> VillageFragment()
            2 -> CagarFragment()
            3 -> TamanFragment()
            4 -> CultureFragment()
            5 -> CulinaryFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}