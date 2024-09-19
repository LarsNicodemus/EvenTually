package com.example.abschlussprojekt.datamodel.localmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "love_facts")
data class LoveFact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fact: String
)