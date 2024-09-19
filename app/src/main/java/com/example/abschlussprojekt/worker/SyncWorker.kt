package com.example.abschlussprojekt.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.abschlussprojekt.data.local.getNotificationDatabase
import com.example.abschlussprojekt.datamodel.localmodels.DateCalculator
import com.example.abschlussprojekt.datamodel.localmodels.EventInfoEntity
import com.example.abschlussprojekt.utils.NotificationScheduler
import com.example.abschlussprojekt.datamodel.localmodels.NotificationTextsEntity
import com.example.abschlussprojekt.datamodel.localmodels.ReminderSettingsEntity
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalEventProfile
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalReminder
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

/**
 * Ein Worker, der Daten von Firebase synchronisiert und Benachrichtigungen plant.
 *
 * @param context Der Kontext, in dem der Worker ausgeführt wird.
 * @param params Die Parameter für den Worker.
 */
class SyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val database = getNotificationDatabase(context)
    private val notificationDao = database.notificationDao()
    private val notificationScheduler = NotificationScheduler(context)

    /**
     * Führt die Arbeit des Workers aus.
     *
     * @return Das Ergebnis der Arbeit.
     */
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val firestore = FirebaseFirestore.getInstance()
            val firebaseAuth = FirebaseAuth.getInstance()
            val userId = firebaseAuth.currentUser?.uid ?: return@withContext Result.failure()

            val reminderTask = firestore.collection("reminder").document(userId).get()
            val eventProfileTask = firestore.collection("event").document(userId).get()
            val notificationTextsTask = firestore.collection("notificationTexts").document(userId).get()

            val results = Tasks.await(Tasks.whenAllSuccess<DocumentSnapshot>(
                reminderTask, eventProfileTask, notificationTextsTask
            ))

            val reminder = results[0].toObject(GlobalReminder::class.java)
            val eventProfile = results[1].toObject(GlobalEventProfile::class.java)
            val notificationTexts = results[2].data as? Map<String, String>

            if (reminder != null && eventProfile != null && notificationTexts != null) {
                // Speichert Erinnerungseinstellungen, Ereignisinformationen und Benachrichtigungstexte in Room
                saveDataToRoom(reminder, eventProfile, notificationTexts)

                // Plant Benachrichtigungen
                scheduleNotifications(reminder, eventProfile.nextAnniversary, notificationTexts)
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error syncing data", e)
            Result.retry()
        }
    }

    /**
     * Speichert Daten in der Room-Datenbank.
     *
     * @param reminder Die Erinnerungseinstellungen.
     * @param eventProfile Die Ereignisinformationen.
     * @param notificationTexts Die Benachrichtigungstexte.
     */
    private fun saveDataToRoom(reminder: GlobalReminder, eventProfile: GlobalEventProfile, notificationTexts: Map<String, String>) {
        // Speichert Erinnerungseinstellungen
        notificationDao.insertReminderSettings(
            ReminderSettingsEntity(
                sevenDaysReminder = reminder.sevenDaysReminder,
                fiveDaysReminder = reminder.fiveDaysReminder,
                threeDaysReminder = reminder.threeDaysReminder,
                oneDayReminder = reminder.oneDayReminder,
                anniversaryReminder = reminder.anniversaryReminder,
                reminderTimeHour = reminder.reminderTimeHour,
                reminderTimeMinute = reminder.reminderTimeMinute
            )
        )

        // Speichert Ereignisinformationen
        notificationDao.insertEventInfo(
            EventInfoEntity(
                nextAnniversary = eventProfile.nextAnniversary,
                startDate = eventProfile.startDate
            )
        )

        // Speichert Benachrichtigungstexte
        notificationDao.insertNotificationTexts(
            NotificationTextsEntity(
                sevenDaysReminderTitle = notificationTexts["sevenDaysReminderTitle"] ?: "7 Days Reminder",
                sevenDaysReminderText = notificationTexts["sevenDaysReminderText"] ?: "7 Days Reminder",
                fiveDaysReminderTitle = notificationTexts["fiveDaysReminderTitle"] ?: "5 Days Reminder",
                fiveDaysReminderText = notificationTexts["fiveDaysReminderText"] ?: "5 Days Reminder",
                threeDaysReminderTitle = notificationTexts["threeDaysReminderTitle"] ?: "3 Days Reminder",
                threeDaysReminderText = notificationTexts["threeDaysReminderText"] ?: "3 Days Reminder",
                oneDayReminderTitle = notificationTexts["oneDayReminderTitle"] ?: "1 Day Reminder",
                oneDayReminderText = notificationTexts["oneDayReminderText"] ?: "1 Day Reminder",
                anniversaryReminderTitle = notificationTexts["anniversaryReminderTitle"] ?: "Anniversary Reminder",
                anniversaryReminderText = notificationTexts["anniversaryReminderText"] ?: "Anniversary Reminder"
            )
        )
    }

    /**
     * Plant Benachrichtigungen basierend auf den Erinnerungseinstellungen und Ereignisinformationen.
     *
     * @param reminder Die Erinnerungseinstellungen.
     * @param nextAnniversary Das Datum des nächsten Jahrestages.
     * @param texts Die Benachrichtigungstexte.
     */
    private fun scheduleNotifications(reminder: GlobalReminder, nextAnniversary: String, texts: Map<String, String>) {
        val dateCalculator = DateCalculator()
        val nextAnniversaryConverted = dateCalculator.convertDateFormat(nextAnniversary)
        val dateParts = nextAnniversaryConverted.split("-")
        Log.d("SyncWorker", "Scheduled: $nextAnniversary")
        if (dateParts.size == 3) {
            val year = dateParts[0].toInt()
            val month = dateParts[1].toInt() - 1 // Kalender-Monate sind 0-indiziert
            val day = dateParts[2].toInt()

            scheduleNotificationIfEnabled(reminder.sevenDaysReminder, year, month, day - 7, reminder, "sevenDays", texts)
            scheduleNotificationIfEnabled(reminder.fiveDaysReminder, year, month, day - 5, reminder, "fiveDays", texts)
            scheduleNotificationIfEnabled(reminder.threeDaysReminder, year, month, day - 3, reminder, "threeDays", texts)
            scheduleNotificationIfEnabled(reminder.oneDayReminder, year, month, day - 1, reminder, "oneDay", texts)
            scheduleNotificationIfEnabled(reminder.anniversaryReminder, year, month, day, reminder, "anniversary", texts)

            // Plant monatliche Benachrichtigung
            scheduleMonthlyNotification(reminder, nextAnniversary, texts)
        }
    }

    /**
     * Plant eine Benachrichtigung, wenn sie aktiviert ist.
     *
     * @param isEnabled Gibt an, ob die Benachrichtigung aktiviert ist.
     * @param year Das Jahr des Benachrichtigungsdatums.
     * @param month Der Monat des Benachrichtigungsdatums.
     * @param day Der Tag des Benachrichtigungsdatums.
     * @param reminder Die Erinnerungseinstellungen.
     * @param type Der Typ der Benachrichtigung.
     * @param texts Die Benachrichtigungstexte.
     */
    private fun scheduleNotificationIfEnabled(isEnabled: Boolean, year: Int, month: Int, day: Int, reminder: GlobalReminder, type: String, texts: Map<String, String>) {
        if (isEnabled) {
            val calendar = Calendar.getInstance().apply {
                set(year, month, day, reminder.reminderTimeHour, reminder.reminderTimeMinute)
            }
            notificationScheduler.scheduleNotification(
                calendar.timeInMillis,
                texts["${type}ReminderTitle"] ?: "$type Reminder",
                texts["${type}ReminderText"] ?: "$type Reminder"
            )
        }
    }

    /**
     * Plant monatliche Benachrichtigungen, wenn sie aktiviert ist.
     *
     * @param reminder Die Erinnerungseinstellungen.
     * @param nextAnniversary Das Datum des nächsten Jahrestages.
     * @param texts Die Benachrichtigungstexte.
     */
    private fun scheduleMonthlyNotification(reminder: GlobalReminder, nextAnniversary: String, texts: Map<String, String>) {
        if (reminder.monthlyReminder) {
            val dateCalculator = DateCalculator()
            val nextAnniversaryConverted = dateCalculator.convertDateFormat(nextAnniversary)
            val dateParts = nextAnniversaryConverted.split("-")
            if (dateParts.size == 3) {
                val day = dateParts[2].toInt()
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, day)
                    set(Calendar.HOUR_OF_DAY, reminder.reminderTimeHour)
                    set(Calendar.MINUTE, reminder.reminderTimeMinute)
                }

                // Plant die Benachrichtigung für die nächsten 12 Monate
                for (i in 1..12) {
                    calendar.add(Calendar.MONTH, 1)
                    notificationScheduler.scheduleNotification(
                        calendar.timeInMillis,
                        texts["monthlyReminderTitle"] ?: "Monthly Reminder",
                        texts["monthlyReminderText"] ?: "This is your monthly reminder."
                    )
                }
            }
        }
    }
}