package com.capstone.project.tourify.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.project.tourify.R

class CategoryAdapter(private val categoryList: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_kategori, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList[position]
        holder.imageView.setImageResource(category.imageResource)
        holder.title.text = category.title
        holder.rating.text = category.rating.toString()
        holder.price.text = category.price
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val title: TextView = itemView.findViewById(R.id.titleCategory)
        val rating: TextView = itemView.findViewById(R.id.ratingText)
        val price: TextView = itemView.findViewById(R.id.priceCategory)
    }
}

data class Category(
    val title: String,
    val rating: Double,
    val price: String,
    val imageResource: Int
)
