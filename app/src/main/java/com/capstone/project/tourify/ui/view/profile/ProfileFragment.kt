package com.capstone.project.tourify.ui.view.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.R
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.databinding.FragmentProfileBinding
import com.capstone.project.tourify.ui.adapter.SettingAdapter
import com.capstone.project.tourify.ui.adapter.SettingItem
import com.capstone.project.tourify.ui.view.MainActivity
import com.capstone.project.tourify.ui.view.about.AboutActivity
import com.capstone.project.tourify.ui.view.editprofile.EditProfileActivity
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
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        userPreference = UserPreference.getInstance(requireContext().applicationContext)

        binding.buttonEdit.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java).apply {
                putExtra("username", binding.tvUsername.text.toString()) // Pass current username
            }
            startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE)
        }

        val settingItems = mutableListOf(
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
                binding.tvUsername.text = user.displayName
                binding.tvEmail.text = user.email
            } else {
                binding.buttonLogin.visibility = View.VISIBLE
                binding.buttonRegister.visibility = View.VISIBLE
                binding.tvUsername.visibility = View.GONE
                binding.tvEmail.visibility = View.GONE
                binding.buttonEdit.visibility = View.GONE

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val user = userPreference.getSession().first()
            binding.tvUsername.text = user.displayName
        }
    }

    private fun handleSettingItemClick(settingItem: SettingItem) {
        when (settingItem.title) {
            "About Us" -> {
                val intent = Intent(requireContext(), AboutActivity::class.java)
                startActivity(intent)
            }
            "Language" ->  {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            "Logout" -> logout()
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            userPreference.logout()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val updatedUsername = data?.getStringExtra("updatedUsername")
            Log.d("ProfileFragment", "onActivityResult: updatedUsername=$updatedUsername")
            if (!updatedUsername.isNullOrEmpty()) {
                binding.tvUsername.text = updatedUsername
            }
        }
    }

    companion object {
        const val EDIT_PROFILE_REQUEST_CODE = 100
    }
}
