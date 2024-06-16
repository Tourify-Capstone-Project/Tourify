package com.capstone.project.tourify.data.local.entity.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val tourismId: String,
    val name: String,
    val imageUrl: String,
    val price: String,
    val userId: String
)
