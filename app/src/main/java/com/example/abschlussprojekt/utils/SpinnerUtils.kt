package com.example.abschlussprojekt.utils

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

object SpinnerUtils {

    /**
     * Richtet den Spinner mit den angegebenen Elementen und Layouts ein.
     *
     * @param context Der Kontext, der zum Erstellen des Adapters verwendet wird.
     * @param spinner Der Spinner, der eingerichtet werden soll.
     * @param itemsArray Die Ressourcen-ID des Arrays mit den Elementen.
     * @param itemLayout Die Ressourcen-ID des Layouts für die einzelnen Elemente.
     * @param dropdownLayout Die Ressourcen-ID des Layouts für das Dropdown-Menü.
     */
    fun setupSpinner(
        context: Context,
        spinner: Spinner,
        itemsArray: Int,
        itemLayout: Int,
        dropdownLayout: Int
    ) {
        ArrayAdapter.createFromResource(
            context,
            itemsArray,
            itemLayout
        ).also { adapter ->
            adapter.setDropDownViewResource(dropdownLayout)
            spinner.adapter = adapter
        }
    }

    /**
     * Setzt den Spinner auf den angegebenen Wert.
     *
     * @param spinner Der Spinner, der gesetzt werden soll.
     * @param value Der Wert, auf den der Spinner gesetzt werden soll.
     */
    fun setSpinnerToValue(spinner: Spinner, value: String?) {
        val adapter = spinner.adapter as? ArrayAdapter<*>
        adapter?.let {
            for (i in 0 until it.count) {
                if (it.getItem(i).toString() == value) {
                    spinner.setSelection(i)
                    return
                }
            }
        }
    }

    /**
     * Richtet einen Listener für den Spinner ein, der auf Elementauswahlen reagiert.
     *
     * @param spinner Der Spinner, für den der Listener eingerichtet werden soll.
     * @param onItemSelected Die Funktion, die aufgerufen wird, wenn ein Element ausgewählt wird.
     * @param onNothingSelected Die Funktion, die aufgerufen wird, wenn nichts ausgewählt wird.
     */
    fun setupSpinnerListener(
        spinner: Spinner,
        onItemSelected: (String) -> Unit,
        onNothingSelected: () -> Unit = {}
    ) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                onItemSelected(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                onNothingSelected()
            }
        }
    }
}