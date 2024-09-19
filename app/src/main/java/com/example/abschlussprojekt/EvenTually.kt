package com.example.abschlussprojekt

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.abschlussprojekt.data.NotificationRepository.Companion.CHANNEL_ID
import com.example.abschlussprojekt.data.NotificationRepository.Companion.CHANNEL_NAME
import com.example.abschlussprojekt.service.AppService

class EvenTually : Application() {
    override fun onCreate() {
        super.onCreate()

        // Erstellen eines NotificationChannels für Android O und höher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val channelID = CHANNEL_ID
            val descriptionText = "Your notification channel description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

//         Starten des AppService
        val serviceIntent = Intent(this, AppService::class.java)
        startService(serviceIntent)
    }
}