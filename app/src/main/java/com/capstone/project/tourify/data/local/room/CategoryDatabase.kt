// CategoryDatabase.kt
package com.capstone.project.tourify.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.project.tourify.data.local.entity.CategoryEntity
import com.capstone.project.tourify.data.local.room.remotekeys.RemoteKeys
import com.capstone.project.tourify.data.local.room.remotekeys.RemoteKeysDao

@Database(entities = [CategoryEntity::class, RemoteKeys::class], version = 2, exportSchema = false)
abstract class CategoryDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: CategoryDatabase? = null

        fun getDatabase(context: Context): CategoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CategoryDatabase::class.java,
                    "category_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
