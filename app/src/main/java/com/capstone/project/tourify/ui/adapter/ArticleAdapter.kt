package com.capstone.project.tourify.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.capstone.project.tourify.R
import com.capstone.project.tourify.data.remote.response.ArticlesResponseItem
import com.capstone.project.tourify.databinding.ItemArticleBinding

class ArticleAdapter: PagingDataAdapter<ArticlesResponseItem, ArticleAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = getItem(position)
        if (news != null) {
            holder.bind(news)
        }
    }

    class MyViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(news: ArticlesResponseItem) {
            binding.tvArticleTitle.text = news.articleTitle
            binding.tvArticleDesc.text = news.articleDesc
            Glide.with(itemView.context)
                .load(news.articlePhotoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.no_image))
                .into(binding.imageArticlePoster)
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(news.articleUrl)
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesResponseItem>() {
            override fun areItemsTheSame(oldItem: ArticlesResponseItem, newItem: ArticlesResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticlesResponseItem, newItem: ArticlesResponseItem): Boolean {
                return oldItem.articleId == newItem.articleId
            }
        }
    }
}