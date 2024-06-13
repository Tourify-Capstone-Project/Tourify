package com.capstone.project.tourify.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.project.tourify.R

class FavoriteAdapter(private val data: List<String>) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.tvTitle)
        val imageTitle: ImageView = view.findViewById(R.id.imagePoster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fav, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTitle.text = data[position]
        holder.imageTitle.setImageResource(R.drawable.no_image)
    }

    override fun getItemCount(): Int = data.size
}