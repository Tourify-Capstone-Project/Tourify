package com.capstone.project.tourify.Helper

import androidx.room.TypeConverter
import com.capstone.project.tourify.data.remote.response.AdditionalImagesItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConverterHelper {
    @TypeConverter
    fun fromAdditionalImagesItemList(value: List<AdditionalImagesItem>): String {
        val gson = Gson()
        val type = object : TypeToken<List<AdditionalImagesItem>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toAdditionalImagesItemList(value: String): List<AdditionalImagesItem> {
        val gson = Gson()
        val type = object : TypeToken<List<AdditionalImagesItem>>() {}.type
        return gson.fromJson(value, type)
    }
}