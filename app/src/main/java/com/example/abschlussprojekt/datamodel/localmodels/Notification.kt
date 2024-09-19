package com.example.abschlussprojekt.datamodel.localmodels

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notification_table")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val yearlyReminder: Boolean = false,
    val monthlyReminder: Boolean = false,
    val sevenDaysReminder: Boolean = false,
    val fiveDaysReminder: Boolean = false,
    val threeDaysReminder: Boolean = false,
    val oneDayReminder: Boolean = false,
    val anniversaryReminder: Boolean = false,
    val reminderTimeHour: Int = 19,
    val reminderTimeMinute: Int = 0,
    val nextAnniversary: String = ""

)
