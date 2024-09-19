// File: app/src/main/java/com/example/abschlussprojekt/datamodel/localmodels/NotificationWorker.kt

package com.example.abschlussprojekt.worker

import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getColor
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.abschlussprojekt.MainActivity
import com.example.abschlussprojekt.R


/**
 * Arbeiterklasse für die Benachrichtigungen.
 *
 * @param context Der Kontext der Anwendung.
 * @param workerParams Die Parameter für den Worker.
 */
class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    private val application = context.applicationContext as Application
    val resources: Resources = application.resources

    /**
     * Führt die Arbeit aus und zeigt die Benachrichtigung an.
     *
     * @return Das Ergebnis der Arbeit.
     */
    override fun doWork(): Result {
        val title = inputData.getString("title") ?: "Standardtitel"
        val message = inputData.getString("message") ?: "Standardnachricht"
        showNotification(title, message)
        return Result.success()
    }


    /**
     * Zeigt die Benachrichtigung an.
     *
     * @param title Der Titel der Benachrichtigung.
     * @param message Die Nachricht der Benachrichtigung.
     */
    private fun showNotification(title: String, message: String) {
        val context = applicationContext
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("openFragment", "HomeFragment")
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification =
            NotificationCompat.Builder(applicationContext, "com.your_app.notifications.channel1")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.eventually121212)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.eventually_231
                    )
                )
                .setColor(getColor(context, R.color.md_theme_primary))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .build()
        notificationManager.notify((0..Int.MAX_VALUE).random(), notification)
    }
}