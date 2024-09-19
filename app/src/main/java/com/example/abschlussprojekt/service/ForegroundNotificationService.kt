package com.example.abschlussprojekt.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.abschlussprojekt.R
import com.example.abschlussprojekt.data.local.NotificationDatabase
import com.example.abschlussprojekt.ui.viewmodels.NotificationViewModel

class ForegroundNotificationService : Service() {

    private lateinit var notificationViewModel: NotificationViewModel

    /**
     * Wird aufgerufen, wenn der Dienst erstellt wird.
     * Initialisiert die Datenbank und den NotificationViewModel.
     * Erstellt einen Benachrichtigungskanal, wenn die Android-Version Oreo oder höher ist.
     */
    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(
            applicationContext,
            NotificationDatabase::class.java, "database-name"
        ).build()
        notificationViewModel = NotificationViewModel(application)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setSound(null, null) // Deaktiviert den Sound für diesen Kanal
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    /**
     * Wird aufgerufen, wenn der Dienst gestartet wird.
     * Erstellt und startet eine Benachrichtigung im Vordergrund.
     *
     * @param intent Das Intent, das den Dienst gestartet hat.
     * @param flags Zusätzliche Daten über den Start.
     * @param startId Eine eindeutige Kennung für diesen spezifischen Startaufruf.
     * @return Der Startmodus für den Dienst.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Service is running")
            .setSmallIcon(R.drawable.eventually121212)
            .build()

        startForeground(1, notification)

        return START_STICKY
    }

    /**
     * Wird aufgerufen, wenn ein Client den Dienst bindet.
     * Da dieser Dienst nicht gebunden ist, wird null zurückgegeben.
     *
     * @param intent Das Intent, das den Bindungsaufruf enthält.
     * @return Ein IBinder-Objekt für die Kommunikation mit dem Client.
     */
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
    }
}