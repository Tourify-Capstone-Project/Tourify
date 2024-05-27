package com.capstone.project.tourify.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.project.tourify.R

class SettingAdapter(private val data: List<SettingItem>) : RecyclerView.Adapter<SettingAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.tvSetting)
        val imageTitle: ImageView = view.findViewById(R.id.iconSetting)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.textViewTitle.text = item.title
        holder.imageTitle.setImageResource(item.imageResId)
    }

    override fun getItemCount(): Int = data.size
}

data class SettingItem(val title: String, val imageResId: Int)