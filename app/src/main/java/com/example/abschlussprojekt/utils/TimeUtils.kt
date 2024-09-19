package com.example.abschlussprojekt.utils

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Locale

object TimeUtils {

    /**
     * Berechnet das Alter basierend auf einem Geburtsdatum-String.
     *
     * @param dateOfBirthString Das Geburtsdatum im Format "DD/MM/YYYY".
     * @return Das berechnete Alter.
     */
    fun calculateAgeFromDateString(dateOfBirthString: String): Int {
        val (day, month, year) = dateOfBirthString.split("/").map { it.toIntOrNull() ?: return 0 }
        val birthDate = Calendar.getInstance().apply { set(year, month - 1, day) }
        return Calendar.getInstance().let { today ->
            today.get(Calendar.YEAR) - year -
                    if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) 1 else 0
        }
    }


    /**
     * Richtet einen Listener für die Datumseingabe ein, der das Datum formatiert.
     *
     * @param textInputEditText Das TextInputEditText, für das der Listener eingerichtet wird.
     */
    fun setupDateInputListener(textInputEditText: TextInputEditText) {
        var current = ""
        val ddmmyyyy = "DDMMYYYY"
        val cal = Calendar.getInstance()

        textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.toString()?.let { newText ->
                    if (newText != current) {
                        val clean = newText.replace(Regex("[^\\d.]|\\."), "")
                        val cleanC = current.replace(Regex("[^\\d.]|\\."), "")

                        var sel = clean.length + (clean.length / 2)
                        if (clean == cleanC) sel--

                        val formattedDate = when {
                            clean.length < 8 -> clean + ddmmyyyy.substring(clean.length)
                            else -> {
                                val day = clean.substring(0, 2).toInt().coerceIn(1, 31)
                                val mon = clean.substring(2, 4).toInt().coerceIn(1, 12)
                                val year = clean.substring(4, 8).toInt().coerceIn(1900, 2100)

                                cal.set(Calendar.MONTH, mon - 1)
                                cal.set(Calendar.YEAR, year)

                                val maxDay = cal.getActualMaximum(Calendar.DATE)
                                val finalDay = day.coerceAtMost(maxDay)

                                String.format(Locale.GERMANY,"%02d%02d%02d", finalDay, mon, year)
                            }
                        }

                        current = String.format(
                            "%s/%s/%s", formattedDate.substring(0, 2),
                            formattedDate.substring(2, 4),
                            formattedDate.substring(4, 8)
                        )

                        textInputEditText.setText(current)
                        textInputEditText.setSelection(sel.coerceIn(0, current.length))
                    }
                }
            }
        })
    }
}
