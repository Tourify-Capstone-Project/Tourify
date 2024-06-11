package com.capstone.project.tourify.ui.view.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.databinding.FragmentFavoriteBinding
import com.capstone.project.tourify.ui.adapter.EstimasiAdapter
import com.capstone.project.tourify.ui.adapter.FavoriteAdapter
import com.capstone.project.tourify.ui.view.login.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreference: UserPreference
    private lateinit var dataList: List<String>
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val view = binding.root

        userPreference = UserPreference.getInstance(requireContext().applicationContext)

        dataList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7")
        adapter = FavoriteAdapter(dataList)
        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            val isLoggedIn = userPreference.getSession().first().isLogin
            updateUI(isLoggedIn)
        }

        return view
    }

    private fun updateUI(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            binding.preLoginLayout.visibility = View.GONE
            binding.postLoginLayout.visibility = View.VISIBLE

            // Load favorite data here if needed
        } else {
            binding.preLoginLayout.visibility = View.VISIBLE
            binding.postLoginLayout.visibility = View.GONE

            // Set login button click listener
            binding.buttonLogin.setOnClickListener {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}