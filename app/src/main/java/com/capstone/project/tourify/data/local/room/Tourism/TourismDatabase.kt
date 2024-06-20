package com.capstone.project.tourify.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.project.tourify.data.local.entity.RecommendedItem

@Database(entities = [RecommendedItem::class], version = 1, exportSchema = false)
abstract class TourismDatabase : RoomDatabase() {

    abstract fun tourismDao(): TourismDao

    companion object {
        @Volatile
        private var INSTANCE: TourismDatabase? = null

        fun getDatabase(context: Context): TourismDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TourismDatabase::class.java,
                    "Tourify_Recommendation"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
