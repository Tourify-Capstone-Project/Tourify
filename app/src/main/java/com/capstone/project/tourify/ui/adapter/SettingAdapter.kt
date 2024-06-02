package com.capstone.project.tourify.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.project.tourify.R

class SettingAdapter(
    private val data: List<SettingItem>,
    private val onItemClickListener: (SettingItem) -> Unit
) : RecyclerView.Adapter<SettingAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.tvSetting)
        val imageTitle: ImageView = view.findViewById(R.id.iconSetting)

        fun bind(settingItem: SettingItem) {
            textViewTitle.text = settingItem.title
            imageTitle.setImageResource(settingItem.imageResId)
            itemView.setOnClickListener {
                onItemClickListener(settingItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size
}
data class SettingItem(val title: String, val imageResId: Int)