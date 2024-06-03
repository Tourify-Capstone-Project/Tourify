package com.capstone.project.tourify.ui.view.estimasi

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.R
import com.capstone.project.tourify.data.remote.pref.UserPreference
import com.capstone.project.tourify.ui.adapter.EstimasiAdapter
import com.capstone.project.tourify.databinding.FragmentEstimasiBinding
import com.capstone.project.tourify.ui.view.login.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EstimasiFragment : Fragment() {

    private var _binding: FragmentEstimasiBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EstimasiAdapter
    private lateinit var dataList: List<String>
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEstimasiBinding.inflate(inflater, container, false)
        val view = binding.root

        userPreference = UserPreference.getInstance(requireContext().applicationContext)

        // Initialize data list and adapter
        dataList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7")
        adapter = EstimasiAdapter(dataList)
        binding.rvPrice.adapter = adapter
        binding.rvPrice.layoutManager = LinearLayoutManager(context)

        // Check login status and set layout visibility
        lifecycleScope.launch {
            val user = userPreference.getSession().first()
            if (user.isLogin) {
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
        }

        setupToolbar()

        return view
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.materialBarEditProfile)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
            title = getString(R.string.adventure_finance)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}