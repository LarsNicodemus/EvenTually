package com.example.abschlussprojekt.utils

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.abschlussprojekt.worker.NotificationWorker
import java.util.concurrent.TimeUnit

/**
 * Klasse zur Planung von Benachrichtigungen.
 *
 * @param context Der Kontext der Anwendung.
 */
class NotificationScheduler(private val context: Context) {

    /**
     * Plant eine Benachrichtigung.
     *
     * @param scheduledDateTime Der geplante Zeitpunkt der Benachrichtigung in Millisekunden.
     * @param title Der Titel der Benachrichtigung.
     * @param message Die Nachricht der Benachrichtigung.
     */
    fun scheduleNotification(
        scheduledDateTime: Long,
        title: String,
        message: String
    ) {
        try {
            val currentTime = System.currentTimeMillis()
            if (scheduledDateTime <= currentTime) {
                Log.e(
                    "NotificationScheduler",
                    "Scheduled time is in the past. Notification not scheduled."
                )
                return
            }
            val delay = scheduledDateTime - currentTime
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()
            val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(workDataOf("title" to title, "message" to message))
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context).enqueue(notificationWork)
            Log.d(
                "NotificationScheduler",
                "Notification scheduled for ${java.util.Date(scheduledDateTime)}: $title"
            )
        } catch (e: Exception) {
            Log.e("NotificationScheduler", "Error scheduling notification: ${e.message}")
        }
    }


}