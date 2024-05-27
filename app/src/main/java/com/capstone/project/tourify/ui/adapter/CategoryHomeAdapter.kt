package com.capstone.project.tourify.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.project.tourify.R

class CategoryHomeAdapter(private val data: List<CategoryItem>) : RecyclerView.Adapter<CategoryHomeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.textCategory)
        val imageTitle: ImageView = view.findViewById(R.id.imageCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.textViewTitle.text = item.title
        holder.imageTitle.setImageResource(item.imageResId)
    }

    override fun getItemCount(): Int = data.size
}

data class CategoryItem(val title: String, val imageResId: Int)
