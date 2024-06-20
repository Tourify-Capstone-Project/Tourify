package com.capstone.project.tourify.ui.view.review

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.project.tourify.data.remote.response.ReviewsItem
import com.capstone.project.tourify.databinding.FragmentReviewBinding
import com.capstone.project.tourify.ui.adapter.ReviewAdapter
import com.capstone.project.tourify.ui.viewmodel.detail.DetailViewModel
import com.capstone.project.tourify.ui.view.review.ReviewActivity
import com.capstone.project.tourify.ui.viewmodelfactory.ViewModelFactory

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DetailViewModel
    private lateinit var reviewAdapter: ReviewAdapter
    private var tourismId: String? = null
    private val REQUEST_UPLOAD_REVIEW = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tourismId = requireActivity().intent.getStringExtra("tourism_id") ?: ""

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext().applicationContext)
        )
            .get(DetailViewModel::class.java)

        setupRecyclerView()

        viewModel.reviews.observe(viewLifecycleOwner, Observer { reviewResponse ->
            reviewResponse.reviews?.let {
                showReviews(it)
            }
        })

        viewModel.fetchReviews(tourismId ?: "")

        binding.btnWriteReview.setOnClickListener {
            val intent = Intent(activity, ReviewActivity::class.java)
            intent.putExtra("tourism_id", tourismId)
            startActivityForResult(intent, REQUEST_UPLOAD_REVIEW)
        }
    }

    private fun setupRecyclerView() {
        reviewAdapter = ReviewAdapter()
        binding.listItemImageReview.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showReviews(reviews: List<ReviewsItem>) {
        if (reviews.isEmpty()) {
            binding.listItemImageReview.visibility = View.GONE
            binding.tvNoReviews.visibility = View.VISIBLE
        } else {
            binding.listItemImageReview.visibility = View.VISIBLE
            binding.tvNoReviews.visibility = View.GONE
            reviewAdapter.submitList(reviews)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_UPLOAD_REVIEW && resultCode == Activity.RESULT_OK) {
            // Refresh reviews after upload
            viewModel.fetchReviews(tourismId ?: "")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
