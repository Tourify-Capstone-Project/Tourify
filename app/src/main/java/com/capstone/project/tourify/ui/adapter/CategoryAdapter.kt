package com.capstone.project.tourify.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.project.tourify.data.local.entity.category.CategoryEntity
import com.capstone.project.tourify.databinding.ListItemKategoriBinding
import com.capstone.project.tourify.ui.view.detail.DetailActivity
import androidx.recyclerview.widget.DiffUtil

class CategoryAdapter(
    private var categoryList: List<CategoryEntity>
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
        val diffCallback = CategoryDiffCallback(categoryList, newCategories)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        categoryList = newCategories
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val binding: ListItemKategoriBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryEntity) {
            Glide.with(binding.imageView.context)
                .load(category.placePhotoUrl)
                .into(binding.imageView)
            binding.titleCategory.text = category.placeName
            binding.ratingText.text = category.rating
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

    class CategoryDiffCallback(
        private val oldList: List<CategoryEntity>,
        private val newList: List<CategoryEntity>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].placeId == newList[newItemPosition].placeId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
