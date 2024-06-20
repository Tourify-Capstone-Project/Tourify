package com.capstone.project.tourify.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.capstone.project.tourify.R
import com.capstone.project.tourify.data.remote.response.ReviewsItem
import com.capstone.project.tourify.databinding.ListItemReviewBinding

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    private var dataList: List<ReviewsItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemReviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = dataList[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int = dataList.size

    fun submitList(dataList: List<ReviewsItem>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ListItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ReviewsItem) {
            binding.jUsername.text = review.username
            binding.jDescription.text = review.reviewDesc
            Glide.with(binding.imgListReview.context)
                .load(review.photoPublicUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.no_profile))
                .into(binding.imgListReview)
        }
    }
}
