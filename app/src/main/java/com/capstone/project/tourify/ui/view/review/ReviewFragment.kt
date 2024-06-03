package com.capstone.project.tourify.ui.view.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.project.tourify.R
import com.capstone.project.tourify.ui.adapter.ReviewAdapter
import com.capstone.project.tourify.ui.adapter.ReviewItem

class ReviewFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_review, container, false)

        val dataList = generateSampleData()
        adapter = ReviewAdapter(dataList)

        recyclerView = rootView.findViewById(R.id.listItemImageReview)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        return rootView
    }

    private fun generateSampleData(): List<ReviewItem> {
        val dataList = mutableListOf<ReviewItem>()
        for (i in 1..5) {
            dataList.add(ReviewItem(R.drawable.saya, "User $i"))
        }
        return dataList
    }

}