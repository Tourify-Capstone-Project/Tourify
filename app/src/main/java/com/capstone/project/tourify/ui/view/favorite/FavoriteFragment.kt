package com.capstone.project.tourify.ui.view.favorite

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.FragmentFavoriteBinding
import com.capstone.project.tourify.ui.adapter.FavoriteAdapter
import com.capstone.project.tourify.ui.view.login.LoginActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize SharedPreferences
        sharedPref = requireActivity().getSharedPreferences("LOGIN", android.content.Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        val dataList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7")

        val adapter = FavoriteAdapter(dataList)
        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.layoutManager = LinearLayoutManager(context)

        // Check login status and set layout visibility
        if (isLoggedIn) {
            binding.preLoginLayout.visibility = View.GONE
            binding.postLoginLayout.visibility = View.VISIBLE
        } else {
            binding.preLoginLayout.visibility = View.VISIBLE
            binding.postLoginLayout.visibility = View.GONE

            // Set login button click listener
            binding.buttonLogin.setOnClickListener {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        setupToolbar()

        return view
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.materialBarEditProfile)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
            title = getString(R.string.string_favorite)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
