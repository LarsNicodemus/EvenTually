package com.example.abschlussprojekt.utils

import androidx.room.TypeConverter


class Converters {
    @TypeConverter
    fun fromStringList(value: String): List<String> {
        return value.split(",").map { it.trim() }
    }

    @TypeConverter
    fun fromListString(list: List<String>): String {
        return list.joinToString(",")
    }


}