package com.capstone.project.tourify.ui.view.photogallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.FragmentPhotoGalleryBinding
import com.capstone.project.tourify.ui.adapter.ImageDetailAdapter


class PhotoGalleryFragment : Fragment() {

    private var _binding: FragmentPhotoGalleryBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoGalleryBinding.inflate(inflater, container, false)
        val view = binding.root

        val images = listOf(
            R.drawable.pengandaran,
            R.drawable.rinjani,
            R.drawable.tangkubangperahu,
            R.drawable.ancol,
            R.drawable.saya
        )

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.listItemImageDetail.layoutManager = layoutManager
        binding.listItemImageDetail.adapter = ImageDetailAdapter(requireContext(), images)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}