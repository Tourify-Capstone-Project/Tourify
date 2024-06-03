package com.capstone.project.tourify.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.project.tourify.R

class ArticleAdapter(private val data: List<ArticleItem>) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.tvArticleTitle)
        val textViewDesc: TextView = view.findViewById(R.id.tvArticleDesc)
        val imageTitle: ImageView = view.findViewById(R.id.imageArticlePoster)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.textViewTitle.text = item.title
        holder.textViewDesc.text = item.description
        holder.imageTitle.setImageResource(item.imageResId)
    }

    override fun getItemCount(): Int = data.size
}

data class ArticleItem(val title: String, val description: String, val imageResId: Int)
