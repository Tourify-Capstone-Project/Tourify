package com.capstone.project.tourify.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.project.tourify.R

class CategoryHomeAdapter(
    private val data: List<CategoryItem>,
    private val onItemClickListener: (CategoryItem) -> Unit
) : RecyclerView.Adapter<CategoryHomeAdapter.ViewHolder>() {


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.textCategory)
        val imageTitle: ImageView = view.findViewById(R.id.imageCategory)

        fun bind(categoryItem: CategoryItem) {
            textViewTitle.text = categoryItem.title
            imageTitle.setImageResource(categoryItem.imageResId)
            itemView.setOnClickListener {
                onItemClickListener(categoryItem)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size
}

data class CategoryItem(val title: String, val imageResId: Int, val id: String)