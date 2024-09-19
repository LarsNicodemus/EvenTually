package com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore

data class GlobalReminder(
    val yearlyReminder: Boolean = false,
    val monthlyReminder: Boolean = false,
    val sevenDaysReminder: Boolean = false,
    val fiveDaysReminder: Boolean = false,
    val threeDaysReminder: Boolean = false,
    val oneDayReminder: Boolean = false,
    val anniversaryReminder: Boolean = false,
    val reminderTimeHour: Int = 19,
    val reminderTimeMinute: Int = 0
)
