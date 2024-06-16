package com.capstone.project.tourify.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.project.tourify.data.local.entity.finance.FinanceEntity
import com.capstone.project.tourify.databinding.ItemPriceBinding
import com.capstone.project.tourify.ui.view.detail.DetailActivity

class EstimasiAdapter(
    private var financeList: List<FinanceEntity>
) : RecyclerView.Adapter<EstimasiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPriceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val finance = financeList[position]
        holder.bind(finance)
    }

    override fun getItemCount(): Int {
        return financeList.size
    }

    inner class ViewHolder(private val binding: ItemPriceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(finance: FinanceEntity) {
            Glide.with(binding.imagePoster.context)
                .load(finance.placePhotoUrl)
                .into(binding.imagePoster)
            binding.tvTitle.text = finance.name
            binding.tvPrice.text = finance.price

            itemView.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("tourism_id", finance.tourismId) // Ensure tourismId is the correct field
                }
                context.startActivity(intent)
            }
        }
    }
}