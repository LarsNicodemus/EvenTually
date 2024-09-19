package com.example.abschlussprojekt.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.example.abschlussprojekt.datamodel.localmodels.LoveFact
import com.example.abschlussprojekt.utils.Converters

@Database(entities = [LoveFact::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LoveFactDatabase : RoomDatabase() {
    abstract val loveFactDao: LoveFactDao
}

private lateinit var INSTANCE: LoveFactDatabase

fun getDatabase(context: Context): LoveFactDatabase {
    synchronized(LoveFactDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                LoveFactDatabase::class.java,
                "love_facts_database"
            )
                .build()
        }
        return INSTANCE
    }
}
