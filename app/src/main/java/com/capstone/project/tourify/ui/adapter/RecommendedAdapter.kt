package com.capstone.project.tourify.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.project.tourify.data.local.entity.RecommendedItem
import com.capstone.project.tourify.databinding.ItemRecommendedBinding
import com.capstone.project.tourify.ui.view.detail.DetailActivity

class RecommendedAdapter(
    private val onItemClickListener: (RecommendedItem) -> Unit
) : RecyclerView.Adapter<RecommendedAdapter.MyViewHolder>() {

    private val data = mutableListOf<RecommendedItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecommendedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recommendedItem = getItem(position)
        recommendedItem?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = data.size

    fun setItems(items: List<RecommendedItem>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: ItemRecommendedBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recommendedItem: RecommendedItem) {
            Glide.with(binding.imagePoster.context)
                .load(recommendedItem.imageUrl)
                .into(binding.imagePoster)

            binding.tvTitle.text = recommendedItem.name
            binding.tvPrice.text = recommendedItem.price
            binding.tvRating.text = recommendedItem.rating

            itemView.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("tourism_id", recommendedItem.tourismId)
                }
                context.startActivity(intent)
                onItemClickListener(recommendedItem)
                Toast.makeText(binding.root.context, "Clicked on: ${recommendedItem.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getItem(position: Int): RecommendedItem? {
        return if (position < data.size) data[position] else null
    }
}
