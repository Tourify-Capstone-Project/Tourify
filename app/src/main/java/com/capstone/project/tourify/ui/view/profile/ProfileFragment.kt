package com.capstone.project.tourify.ui.view.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.FragmentProfileBinding
import com.capstone.project.tourify.ui.adapter.SettingAdapter
import com.capstone.project.tourify.ui.adapter.SettingItem

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingAdapter: SettingAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.buttonEdit.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_nav_profile_to_editProfileActivity)
        )

        val settingItems = listOf(
            SettingItem("About Us", R.drawable.info_light),
            SettingItem("Language", R.drawable.global_search),
            SettingItem("Logout", R.drawable.logout_light),
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

            "Language" -> settingLanguage()

            "Logout" -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_nav_profile_to_loginActivity)
            }
        }
    }

    private fun settingLanguage() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}