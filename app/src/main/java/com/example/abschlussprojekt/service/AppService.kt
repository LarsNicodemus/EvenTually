package com.example.abschlussprojekt.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AppService : Service() {

    /**
     * Wird aufgerufen, wenn die Aufgabe entfernt wird.
     * Startet den `ForegroundNotificationService`.
     *
     * @param rootIntent Das Intent, das die Aufgabe entfernt hat.
     */
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        val serviceIntent = Intent(this, ForegroundNotificationService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    /**
     * Bindet den Service an einen Client.
     *
     * @param intent Das Intent, das den Service bindet.
     * @return Ein IBinder-Objekt, das die Schnittstelle zum Client darstellt.
     */
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}