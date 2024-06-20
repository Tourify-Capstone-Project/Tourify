package com.capstone.project.tourify.ui.view.review

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

import com.capstone.project.tourify.ui.viewmodelfactory.ViewModelFactory

class ReviewFragment : Fragment(), WriteReviewDialogFragment.WriteReviewDialogListener {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DetailViewModel
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tourismId = requireActivity().intent.getStringExtra("tourism_id") ?: ""

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

        viewModel.fetchReviews(tourismId)

        // Tampilkan dialog write review saat fragment pertama kali ditampilkan
        binding.btnWriteReview.setOnClickListener() {
            showWriteReviewDialog()
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
        reviewAdapter.submitList(reviews)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onReviewSubmitted(review: String) {
        // Implement logic to submit review to ViewModel or repository
        // For example:
        // viewModel.submitReview(review)
        // Then refresh reviews list if needed
    }

    private fun showWriteReviewDialog() {
        val dialogFragment = WriteReviewDialogFragment()
        dialogFragment.setWriteReviewDialogListener(this)
        dialogFragment.show(parentFragmentManager, "write_review_dialog")
    }
}
