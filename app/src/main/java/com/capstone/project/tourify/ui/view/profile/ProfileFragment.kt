package com.capstone.project.tourify.ui.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.R
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.databinding.FragmentProfileBinding
import com.capstone.project.tourify.ui.adapter.SettingAdapter
import com.capstone.project.tourify.ui.adapter.SettingItem
import com.capstone.project.tourify.ui.view.login.LoginActivity
import com.capstone.project.tourify.ui.view.onboardingpage.OnBoardingActivity
import com.capstone.project.tourify.ui.view.register.RegisterActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingAdapter: SettingAdapter
    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        userPreference = UserPreference.getInstance(requireContext().applicationContext)

        binding.buttonEdit.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_nav_profile_to_editProfileActivity)
        )

        val settingItems = mutableListOf<SettingItem>(
            SettingItem("About Us", R.drawable.info_light),
            SettingItem("Language", R.drawable.global_search),
        )

        lifecycleScope.launch {
            val user = userPreference.getSession().first()
            if (user.isLogin) {
                settingItems.add(SettingItem("Logout", R.drawable.logout_light))
                binding.buttonLogin.visibility = View.GONE
                binding.buttonRegister.visibility = View.GONE
                binding.tvUsername.visibility = View.VISIBLE
                binding.tvEmail.visibility = View.VISIBLE
                binding.buttonEdit.visibility = View.VISIBLE
                binding.tvUsername.text = user.email // Assuming the username is the email
                binding.tvEmail.text = user.email
            } else {
                binding.buttonLogin.visibility = View.VISIBLE
                binding.buttonRegister.visibility = View.VISIBLE
                binding.tvUsername.visibility = View.GONE
                binding.tvEmail.visibility = View.GONE
                binding.buttonEdit.visibility = View.GONE

                // Set login button click listener
                binding.buttonLogin.setOnClickListener {
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                }

                binding.buttonRegister.setOnClickListener {
                    val intent = Intent(activity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }

            settingAdapter = SettingAdapter(settingItems, this@ProfileFragment::handleSettingItemClick)

            with(binding.rvSetting) {
                adapter = settingAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }

        return view
    }

    private fun handleSettingItemClick(settingItem: SettingItem) {
        when (settingItem.title) {
            "About Us" -> Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_nav_profile_to_aboutActivity)

            "Logout" -> logout()
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            userPreference.logout()
            // Navigate back to OnBoardingActivity
            val intent = Intent(requireContext(), OnBoardingActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // Finish the activity to prevent the user from navigating back to it using the back button
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
