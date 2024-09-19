package com.example.abschlussprojekt.datamodel.localmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_info")
data class EventInfoEntity(
    @PrimaryKey val id: String = "event_info",
    val nextAnniversary: String,
    val startDate: String
)
