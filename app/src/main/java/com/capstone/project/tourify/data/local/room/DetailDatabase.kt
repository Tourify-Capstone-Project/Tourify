package com.capstone.project.tourify.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.capstone.project.tourify.Helper.ConverterHelper
import com.capstone.project.tourify.data.remote.response.DetailResponse

@Database(entities = [DetailResponse::class], version = 1, exportSchema = false)
@TypeConverters(ConverterHelper::class)
abstract class DetailDatabase : RoomDatabase() {
    abstract fun detailDao(): DetailDao

    companion object {
        @Volatile
        private var INSTANCE: DetailDatabase? = null

        fun getDatabase(context: Context): DetailDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DetailDatabase::class.java,
                    "detail_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
