package com.capstone.project.tourify.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.project.tourify.data.local.entity.CategoryEntity
import com.capstone.project.tourify.databinding.ListItemKategoriBinding

class CategoryAdapter(
    private var categoryList: List<CategoryEntity>,
    private val onItemClick: (CategoryEntity) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemKategoriBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun updateCategories(newCategories: List<CategoryEntity>) {
        categoryList = newCategories
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ListItemKategoriBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryEntity) {
            Glide.with(binding.imageView.context)
                .load(category.placePhotoUrl)
                .into(binding.imageView)
            binding.titleCategory.text = category.placeName
            binding.ratingText.text = category.rating
            binding.priceCategory.text = category.price

            itemView.setOnClickListener { onItemClick(category) }
        }
    }
}
