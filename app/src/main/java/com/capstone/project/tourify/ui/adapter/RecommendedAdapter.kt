package com.capstone.project.tourify.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.project.tourify.R

class RecommendedAdapter(
    private val data: List<RecommendedItem>,
    private val onItemClickListener: (RecommendedItem) -> Unit
) : RecyclerView.Adapter<RecommendedAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.tvTitle)
        val imageTitle: ImageView = view.findViewById(R.id.imagePoster)

        fun bind(recommendedItem: RecommendedItem) {
            textViewTitle.text = recommendedItem.title
            imageTitle.setImageResource(recommendedItem.imageResId)
            itemView.setOnClickListener {
                onItemClickListener(recommendedItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommended, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size
}
data class RecommendedItem(val title: String, val imageResId: Int)