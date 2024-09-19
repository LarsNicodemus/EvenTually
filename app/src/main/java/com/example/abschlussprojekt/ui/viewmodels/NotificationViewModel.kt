package com.example.abschlussprojekt.ui.viewmodels

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.example.abschlussprojekt.data.FirebaseRepository
import com.example.abschlussprojekt.data.NotificationRepository
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalReminder
import com.google.firebase.firestore.DocumentReference

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    private val notificationRepository = NotificationRepository(application)
    private val firebaseRepository = FirebaseRepository()

    var reminderRef: DocumentReference? = firebaseRepository.reminderRef
    val eventDateStrings = notificationRepository.eventDateString


    /**
     * Plant den Synchronisierungs-Worker.
     * @param context Der Kontext der Anwendung.
     */
    fun scheduleSyncWorker(context: Context) {
        notificationRepository.scheduleSyncWorker()
    }

    /**
     * Erstellt einen Benachrichtigungskanal.
     * @param context Der Kontext der Anwendung.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context) {
        notificationRepository.createNotificationChannel()
    }

    /**
     * Überprüft die Benachrichtigungsberechtigungen.
     * @param context Der Kontext der Anwendung.
     * @return True, wenn die Berechtigungen erteilt sind, sonst False.
     */
    fun checkNotificationPermission(context: Context): Boolean {
        return notificationRepository.checkNotificationPermissions()
    }

    /**
     * Aktualisiert die Erinnerungen in Firebase.
     * @param reminder Die neuen Erinnerungen.
     */
    fun firebaseUpdateReminder(reminder: GlobalReminder) {
        firebaseRepository.firebaseUpdateReminder(reminder)
    }

    /**
     * Aktualisiert ein bestimmtes Feld der Erinnerungen in Firebase.
     * @param field Das zu aktualisierende Feld.
     * @param value Der neue Wert des Feldes.
     */
    fun firebaseUpdatePartialReminder(field: String, value: Any) {
        firebaseRepository.firebaseUpdatePartialReminder(field, value)
    }

    /**
     * Holt die Erinnerungsdaten aus Firebase.
     */
    fun fetchReminderData() {
        firebaseRepository.fetchReminderData()
    }

    /**
     * Aktualisiert das Datum des Ereignisses.
     * @param newDate Das neue Datum des Ereignisses.
     */
    fun updateEventDateString(newDate: String) {
        notificationRepository.updateEventDateString(newDate)
    }

    /**
     * Überprüft und fordert ggf. eine Ausnahme von der Batterieoptimierung an.
     * @param context Der Kontext der Anwendung.
     */
    fun checkAndRequestBatteryOptimization(context: Context) {
        notificationRepository.checkAndRequestBatteryOptimization()
    }

    /**
     * Fordert eine Ausnahme von der Batterieoptimierung an.
     * @param context Der Kontext der Anwendung.
     */
    fun requestBatteryOptimizationExemption(context: Context) {
        notificationRepository.requestBatteryOptimizationExemption()
    }

    fun cancelAllScheduledNotifications(){
        notificationRepository.cancelAllScheduledNotifications()
    }
}