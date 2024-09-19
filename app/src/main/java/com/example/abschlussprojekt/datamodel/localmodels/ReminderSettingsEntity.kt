package com.example.abschlussprojekt.datamodel.localmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_settings")
data class ReminderSettingsEntity(
    @PrimaryKey val id: String = "reminder_settings",
    val sevenDaysReminder: Boolean,
    val fiveDaysReminder: Boolean,
    val threeDaysReminder: Boolean,
    val oneDayReminder: Boolean,
    val anniversaryReminder: Boolean,
    val reminderTimeHour: Int,
    val reminderTimeMinute: Int
)
