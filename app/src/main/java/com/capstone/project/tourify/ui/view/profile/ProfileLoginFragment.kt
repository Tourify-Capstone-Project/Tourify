package com.capstone.project.tourify.ui.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.FragmentProfileBinding
import com.capstone.project.tourify.databinding.FragmentProfileLoginBinding
import com.capstone.project.tourify.ui.adapter.SettingAdapter
import com.capstone.project.tourify.ui.adapter.SettingItem

class ProfileLoginFragment : Fragment() {

    private var _binding: FragmentProfileLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        val settingItems = listOf(
            SettingItem("About Us", R.drawable.info_light),
            SettingItem("Language", R.drawable.global_search)
        )

        val settingAdapter = SettingAdapter(settingItems) { settingItem ->
            handleSettingItemClick(settingItem)
        }

        binding.rvSetting.adapter = settingAdapter
        binding.rvSetting.layoutManager = LinearLayoutManager(context)
        return view
    }

    private fun handleSettingItemClick(settingItem: SettingItem) {
        when (settingItem.title) {
            "About Us" -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_nav_profile_to_aboutActivity)
            }
        }
    }
}