package com.example.abschlussprojekt.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.abschlussprojekt.datamodel.localmodels.EventInfoEntity
import com.example.abschlussprojekt.datamodel.localmodels.NotificationEntity
import com.example.abschlussprojekt.datamodel.localmodels.NotificationTextsEntity
import com.example.abschlussprojekt.datamodel.localmodels.ReminderSettingsEntity


@Database(entities = [NotificationEntity::class, ReminderSettingsEntity::class, EventInfoEntity::class, NotificationTextsEntity::class], version = 1, exportSchema = false)
abstract class NotificationDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}

private lateinit var INSTANCE: NotificationDatabase

fun getNotificationDatabase(context: Context): NotificationDatabase {
    synchronized(NotificationDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                NotificationDatabase::class.java,
                "notification_database"
            ).build()
        }
        return INSTANCE
    }
}