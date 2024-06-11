package com.capstone.project.tourify.ui.view.overview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.project.tourify.databinding.FragmentOverviewBinding

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString("placeDesc")?.let { placeDesc ->
            Log.d("OverviewFragment", "placeDesc: $placeDesc")
            binding.textOverview.text = placeDesc
        } ?: run {
            Log.d("OverviewFragment", "No placeDesc found in arguments")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
