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
import com.capstone.project.tourify.ui.view.MainActivity
import com.capstone.project.tourify.ui.view.login.LoginActivity
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
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        userPreference = UserPreference.getInstance(requireContext().applicationContext)

        setupUI()

        return view
    }

    private fun setupUI() {
        binding.buttonEdit.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_nav_profile_to_editProfileActivity)
        )

        val settingItems = mutableListOf(
            SettingItem("About Us", R.drawable.info_light),
            SettingItem("Language", R.drawable.global_search)
        )

        lifecycleScope.launch {
            val user = userPreference.getSession().first()
            if (user.isLogin) {
                settingItems.add(SettingItem("Logout", R.drawable.logout_light))
                updateUIForLoggedInUser(user.email)
            } else {
                updateUIForLoggedOutUser()
            }

            setupRecyclerView(settingItems)
        }
    }

    private fun updateUIForLoggedInUser(email: String) {
        binding.apply {
            buttonLogin.visibility = View.GONE
            buttonRegister.visibility = View.GONE
            tvUsername.visibility = View.VISIBLE
            tvEmail.visibility = View.VISIBLE
            buttonEdit.visibility = View.VISIBLE
            tvUsername.text = email
            tvEmail.text = email
        }
    }

    private fun updateUIForLoggedOutUser() {
        binding.apply {
            buttonLogin.visibility = View.VISIBLE
            buttonRegister.visibility = View.VISIBLE
            tvUsername.visibility = View.GONE
            tvEmail.visibility = View.GONE
            buttonEdit.visibility = View.GONE

            buttonLogin.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }

            buttonRegister.setOnClickListener {
                startActivity(Intent(activity, RegisterActivity::class.java))
            }
        }
    }

    private fun setupRecyclerView(settingItems: List<SettingItem>) {
        settingAdapter = SettingAdapter(settingItems, this::handleSettingItemClick)
        binding.rvSetting.apply {
            adapter = settingAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreference = UserPreference.getInstance(requireContext())
        lifecycleScope.launch {
            val user = userPreference.getSession().first()
            binding.tvUsername.text = user.displayName
        }
    }

    private fun handleSettingItemClick(settingItem: SettingItem) {
        when (settingItem.title) {
            "About Us" -> Navigation.findNavController(requireActivity(), R.id.viewPager)
                .navigate(R.id.action_nav_profile_to_aboutActivity)
            "Logout" -> logout()
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            userPreference.logout()
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
