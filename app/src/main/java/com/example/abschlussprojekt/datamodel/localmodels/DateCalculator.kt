package com.example.abschlussprojekt.datamodel.localmodels

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


/**
 * Klasse zur Berechnung von Datums- und Zeitdifferenzen.
 */
class DateCalculator {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var updateRunnable: Runnable


    /**
     * Berechnet und aktualisiert den Countdown und die Zeit seit einem Ereignis.
     *
     * @param dateString Das Datum des Ereignisses im Format "dd/MM/yyyy".
     * @param updateCallback Die Callback-Funktion zur Aktualisierung der Anzeige.
     */
    fun calculateAndUpdate(dateString: String, updateCallback: (String, String) -> Unit) {
        val eventDate = dateFormat.parse(dateString) ?: return

        updateRunnable = object : Runnable {
            override fun run() {
                val currentDate = Calendar.getInstance()

                // Countdown bis zum nächsten Ereignis
                val nextEventDate = calculateNextEventDate(eventDate, currentDate)
                val countdownMillis = nextEventDate.timeInMillis - currentDate.timeInMillis
                val countdown = formatDuration(countdownMillis)

                // Zeit seit dem Ereignis
                val timeSinceEvent = calculateTimeSinceEvent(eventDate, currentDate)

                updateCallback(countdown, timeSinceEvent)

                handler.postDelayed(this, 1000) // Aktualisiere jede Sekunde
            }
        }

        handler.post(updateRunnable)
    }

    /**
     * Berechnet das Datum des nächsten Ereignisses.
     *
     * @param eventDate Das Datum des Ereignisses.
     * @param currentDate Das aktuelle Datum.
     * @return Das Datum des nächsten Ereignisses.
     */
    private fun calculateNextEventDate(eventDate: Date, currentDate: Calendar): Calendar {
        val nextEventDate = Calendar.getInstance()
        nextEventDate.time = eventDate
        nextEventDate.set(Calendar.YEAR, currentDate.get(Calendar.YEAR))

        if (nextEventDate.before(currentDate)) {
            nextEventDate.add(Calendar.YEAR, 1)
        }

        return nextEventDate
    }

    /**
     * Berechnet die Zeit seit einem Ereignis.
     *
     * @param eventDate Das Datum des Ereignisses.
     * @param currentDate Das aktuelle Datum.
     * @return Die Zeit seit dem Ereignis als String.
     */
    private fun calculateTimeSinceEvent(eventDate: Date, currentDate: Calendar): String {
        val diffMillis = currentDate.timeInMillis - eventDate.time
        val years = TimeUnit.MILLISECONDS.toDays(diffMillis) / 365
        val months = (TimeUnit.MILLISECONDS.toDays(diffMillis) % 365) / 30
        val days = TimeUnit.MILLISECONDS.toDays(diffMillis) % 30
        val hours = TimeUnit.MILLISECONDS.toHours(diffMillis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis) % 60

        return "$years Jahre, $months Monate, $days Tage, $hours Stunden, $minutes Minuten"
    }

    /**
     * Formatiert eine Dauer in Millisekunden als String.
     *
     * @param millis Die Dauer in Millisekunden.
     * @return Die formatierte Dauer als String.
     */
    private fun formatDuration(millis: Long): String {
        val days = TimeUnit.MILLISECONDS.toDays(millis)
        val hours = TimeUnit.MILLISECONDS.toHours(millis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

        return "$days Tage, $hours Stunden, $minutes Minuten, $seconds Sekunden"
    }

    /**
     * Berechnet die Anzahl der Tage bis zum nächsten Jahrestag.
     *
     * @param dateString Das Datum des Ereignisses im Format "dd/MM/yyyy".
     * @return Die Anzahl der Tage bis zum nächsten Jahrestag.
     */
    fun daysUntilNextAnniversary(dateString: String): Int {
        val eventDate = dateFormat.parse(dateString) ?: return -1
        val currentDate = Calendar.getInstance()


        val nextEventDate = calculateNextEventDate(eventDate, currentDate)

        val diffMillis = nextEventDate.timeInMillis - currentDate.timeInMillis

        return TimeUnit.MILLISECONDS.toDays(diffMillis).toInt()
    }

    /**
     * Gibt das Datum des nächsten Jahrestages zurück.
     *
     * @param dateString Das Datum des Ereignisses im Format "dd/MM/yyyy".
     * @return Das Datum des nächsten Jahrestages als String.
     */
    fun getNextAnniversaryDate(dateString: String): String? {
        val eventDate = dateFormat.parse(dateString) ?: return null
        val currentDate = Calendar.getInstance()
        val nextEventDate = calculateNextEventDate(eventDate, currentDate)

        return dateFormat.format(nextEventDate.time)
    }

    /**
     * Konvertiert ein Datum vom Format "dd/MM/yyyy" in das Format "yyyy-MM-dd".
     *
     * @param inputDate Das Eingabedatum im Format "dd/MM/yyyy".
     * @return Das konvertierte Datum im Format "yyyy-MM-dd".
     */
    fun convertDateFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        Log.d("HomeFragment", "date: ${date?.let { outputFormat.format(it) }}")
        return date?.let { outputFormat.format(it) } ?: "Ungültiges Datum"
    }

    fun stopUpdates() {
        if (::updateRunnable.isInitialized) {
            handler.removeCallbacks(updateRunnable)
        }
    }
}