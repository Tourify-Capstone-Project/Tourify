package com.capstone.project.tourify.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recommended_items")
data class RecommendedItem (
    @PrimaryKey val id: String,
    val tourismId: String,
    val name: String,
    val imageUrl: String,
    val price: String,
    val rating: String,
    val userId: String
)
