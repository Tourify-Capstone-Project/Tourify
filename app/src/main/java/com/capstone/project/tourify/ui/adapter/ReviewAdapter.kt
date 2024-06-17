package com.capstone.project.tourify.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.project.tourify.R
import de.hdodenhof.circleimageview.CircleImageView

class ReviewAdapter(private val dataList: List<ReviewItem>) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.imgListReview.setImageResource(currentItem.imageResource)
        holder.userName.text = currentItem.userName

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgListReview = itemView.findViewById<CircleImageView>(R.id.img_list_review)!!
        val userName = itemView.findViewById<TextView>(R.id.j_username)!!
    }
}

data class ReviewItem(val imageResource: Int, val userName: String)