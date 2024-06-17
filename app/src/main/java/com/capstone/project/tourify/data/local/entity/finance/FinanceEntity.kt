package com.capstone.project.tourify.data.local.entity.finance

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "finance")
data class FinanceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val price: String,
    val userId: String,
    val tourismId: String,
    val city: String,
    val rating: String,
    val placePhotoUrl: String,
    val totalCost: String,
)