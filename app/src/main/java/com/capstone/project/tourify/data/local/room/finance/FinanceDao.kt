package com.capstone.project.tourify.data.local.room.finance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.capstone.project.tourify.data.local.entity.finance.FinanceEntity

@Dao
interface FinanceDao {
    @Insert
    suspend fun insert(finance: FinanceEntity)

    @Query("SELECT * FROM finance")
    suspend fun getAllFinance(): List<FinanceEntity>
}