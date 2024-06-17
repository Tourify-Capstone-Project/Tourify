package com.capstone.project.tourify.ui.view.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.data.local.entity.favorite.FavoriteEntity
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.databinding.FragmentFavoriteBinding
import com.capstone.project.tourify.ui.adapter.FavoriteAdapter
import com.capstone.project.tourify.ui.view.login.LoginActivity
import com.capstone.project.tourify.ui.viewmodel.favorite.FavoriteViewModel
import com.capstone.project.tourify.ui.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreference: UserPreference
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val view = binding.root

        // Set up the ViewModel
        val factory = ViewModelFactory.getInstance(requireContext().applicationContext)
        favoriteViewModel = ViewModelProvider(this, factory).get(FavoriteViewModel::class.java)

        userPreference = UserPreference.getInstance(requireContext().applicationContext)

        binding.rvFavorite.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            val session = userPreference.getSession().first()
            updateUI(session.isLogin)
            if (session.isLogin) {
                favoriteViewModel.loadFavorites()
            }
        }

        favoriteViewModel.favorites.observe(viewLifecycleOwner) { response ->
            response?.let {
                favoriteAdapter = FavoriteAdapter(it.detailsFavorite.map { item ->
                    FavoriteEntity(
                        id = item.favoriteId,
                        name = item.detailPlace.placeName,
                        imageUrl = item.detailPlace.placePhotoUrl,
                        price = item.detailPlace.price,
                        userId = item.userId,
                        tourismId = item.tourismId
                    )
                })
                binding.rvFavorite.adapter = favoriteAdapter
            } ?: run {
                // Handle null response, e.g., show a message to the user
            }
        }

        return view
    }

    private fun updateUI(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            binding.preLoginLayout.visibility = View.GONE
            binding.postLoginLayout.visibility = View.VISIBLE
        } else {
            binding.preLoginLayout.visibility = View.VISIBLE
            binding.postLoginLayout.visibility = View.GONE

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
