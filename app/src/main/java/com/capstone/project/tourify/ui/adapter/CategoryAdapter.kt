package com.capstone.project.tourify.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.project.tourify.data.local.entity.category.CategoryEntity
import com.capstone.project.tourify.databinding.ListItemKategoriBinding
import com.capstone.project.tourify.ui.view.detail.DetailActivity

class CategoryAdapter : PagingDataAdapter<CategoryEntity, CategoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemKategoriBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = getItem(position)
        category?.let {
            holder.bind(it)
        }
    }

    class MyViewHolder(private val binding: ListItemKategoriBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CategoryEntity) {
            Glide.with(binding.imageView.context)
                .load(category.placePhotoUrl)
                .into(binding.imageView)

            binding.titleCategory.text = category.placeName
            binding.ratingText.text = category.rating.toString()
            binding.priceCategory.text = category.price

            itemView.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("tourism_id", category.placeId)
                }
                context.startActivity(intent)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CategoryEntity>() {
            override fun areItemsTheSame(oldItem: CategoryEntity, newItem: CategoryEntity): Boolean {
                return oldItem.placeId == newItem.placeId
            }

            override fun areContentsTheSame(oldItem: CategoryEntity, newItem: CategoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}