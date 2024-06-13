package com.capstone.project.tourify.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.project.tourify.R
import com.capstone.project.tourify.databinding.ListItemTabsDetailBinding

class TabsAdapter(
    private var tabs: List<String>,
    private val onTabClick: (Int) -> Unit
) : RecyclerView.Adapter<TabsAdapter.TabsViewHolder>() {

    private var selectedPosition = 0

    inner class TabsViewHolder(private val binding: ListItemTabsDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tab: String, position: Int) {
            binding.itemButton.text = tab
            binding.itemButton.setBackgroundColor(
                if (selectedPosition == position) itemView.context.getColor(R.color.Blue_purple)
                else itemView.context.getColor(R.color.white)
            )
            binding.itemButton.setTextColor(
                if (selectedPosition == position) itemView.context.getColor(R.color.white)
                else itemView.context.getColor(R.color.black)
            )

            binding.itemButton.setOnClickListener {
                onTabClick(position)
                val previousPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousPosition)
                notifyItemChanged(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabsViewHolder {
        val binding =
            ListItemTabsDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TabsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TabsViewHolder, position: Int) {
        holder.bind(tabs[position], position)
    }

    override fun getItemCount(): Int = tabs.size

    fun updateCategories(newCategories: List<String>) {
        tabs = newCategories
        notifyDataSetChanged()
    }

    fun setSelectedPosition(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(position)
    }
}
