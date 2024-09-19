package com.example.abschlussprojekt.datamodel.localmodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val scheduledDateTime: Long,
    val reminderType: String // z.B. "sevenDays", "fiveDays", "threeDays", "oneDay", "anniversary"
)





