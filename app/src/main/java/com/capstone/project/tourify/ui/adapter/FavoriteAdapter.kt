package com.capstone.project.tourify.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.project.tourify.data.local.entity.favorite.FavoriteEntity
import com.capstone.project.tourify.databinding.ItemFavBinding
import com.capstone.project.tourify.ui.view.detail.DetailActivity

class FavoriteAdapter(
    private var favoriteList: List<FavoriteEntity>
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = favoriteList[position]
        holder.bind(favorite)
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    inner class ViewHolder(private val binding: ItemFavBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: FavoriteEntity) {
            Glide.with(binding.imagePoster.context)
                .load(favorite.imageUrl)
                .into(binding.imagePoster)
            binding.tvTitle.text = favorite.name
            binding.tvPrice.text = favorite.price

            itemView.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("tourism_id", favorite.tourismId) // Ensure tourismId is the correct field
                }
                context.startActivity(intent)
            }
        }
    }
}
