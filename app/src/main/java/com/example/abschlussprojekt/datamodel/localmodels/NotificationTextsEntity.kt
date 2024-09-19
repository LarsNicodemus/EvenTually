package com.example.abschlussprojekt.datamodel.localmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_texts")
data class NotificationTextsEntity(
    @PrimaryKey val id: String = "notification_texts",
    val sevenDaysReminderTitle: String,
    val sevenDaysReminderText: String,
    val fiveDaysReminderTitle: String,
    val fiveDaysReminderText: String,
    val threeDaysReminderTitle: String,
    val threeDaysReminderText: String,
    val oneDayReminderTitle: String,
    val oneDayReminderText: String,
    val anniversaryReminderTitle: String,
    val anniversaryReminderText: String
)
