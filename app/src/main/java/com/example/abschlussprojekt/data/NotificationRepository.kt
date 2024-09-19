package com.example.abschlussprojekt.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.abschlussprojekt.worker.SyncWorker

class NotificationRepository(private val context: Context) {

    private val _eventDateString = MutableLiveData<String?>()
    val eventDateString: LiveData<String?> get() = _eventDateString

    /**
     * Aktualisiert den Ereignisdatum-String.
     *
     * @param newDate Der neue Ereignisdatum-String.
     */
    fun updateEventDateString(newDate: String) {
        _eventDateString.value = newDate
    }

    /**
     * Fordert eine Ausnahme von der Batterieoptimierung an.
     */
    fun requestBatteryOptimizationExemption() {
        val packageName = context.packageName
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = Uri.parse("package:$packageName")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Diese Zeile hinzufügen, um das Problem zu beheben
            }
            context.startActivity(intent)
        }
    }

    /**
     * Plant den SyncWorker zur einmaligen Ausführung.
     */
    fun scheduleSyncWorker() {
        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
        WorkManager.getInstance(context).enqueue(syncWorkRequest)
    }

    /**
     * Bricht alle geplanten Benachrichtigungen ab.
     */
    fun cancelAllScheduledNotifications() {
        try {
            WorkManager.getInstance(context).cancelAllWork()
            Log.d("NotificationRepository", "Alle Benachrichtigungen abgebrochen")
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Fehler beim Abbrechen der Benachrichtigungen: ${e.message}")
        }
    }

    /**
     * Erstellt einen Benachrichtigungskanal für Android O und höher.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel() {
        val name = CHANNEL_NAME
        val channelID = CHANNEL_ID
        val descriptionText = "Ihre Benachrichtigungskanalbeschreibung"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Überprüft, ob Benachrichtigungsberechtigungen erteilt wurden, und fordert sie an, falls nicht.
     *
     * @return True, wenn Berechtigungen erteilt wurden, andernfalls false.
     */
    fun checkNotificationPermissions(): Boolean {
        val isEnabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.areNotificationsEnabled()
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }

        if (!isEnabled) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Diese Zeile hinzufügen, um das Problem zu beheben
            }
            context.startActivity(intent)
            Log.d("NotificationRepository", "Benachrichtigungsberechtigungen nicht erteilt")
            return false
        }

        Log.d("NotificationRepository", "Benachrichtigungsberechtigungen erteilt")
        return true
    }

    /**
     * Überprüft und fordert eine Ausnahme von der Batterieoptimierung an, falls noch nicht erteilt.
     */
    fun checkAndRequestBatteryOptimization() {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(context.packageName)) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = Uri.parse("package:${context.packageName}")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Diese Zeile hinzufügen, um das Problem zu beheben
            }
            context.startActivity(intent)
        }
    }

    companion object {
        const val CHANNEL_ID = "notification_channel"
        const val CHANNEL_NAME = "com.example.eventually"
    }
}