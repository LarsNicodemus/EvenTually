package com.example.abschlussprojekt.utils

import android.content.Context
import androidx.viewbinding.ViewBinding
import com.example.abschlussprojekt.data.local.PartnerDetails
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

object ChipUtils {

    /**
     * Richtet die Chip-Gruppen ein und fügt Chips basierend auf den PartnerDetails hinzu.
     *
     * @param binding Das ViewBinding-Objekt, das die Views enthält.
     * @param context Der Kontext, der zum Erstellen der Chips verwendet wird.
     * @param chipGroup Die erste Chip-Gruppe für Likes.
     * @param chipGroup2 Die zweite Chip-Gruppe für Essensvorlieben.
     * @param chipGroup3 Die dritte Chip-Gruppe für Aktivitätsvorlieben.
     */
    fun setupChipGroups(
        binding: ViewBinding,
        context: Context,
        chipGroup: ChipGroup,
        chipGroup2: ChipGroup,
        chipGroup3: ChipGroup
    ) {
        binding.apply {


            PartnerDetails.likes.forEach { like ->
                val chip = Chip(context)
                chip.text = like
                chip.isClickable = true
                chip.isCheckable = true
                chipGroup.addView(chip)
            }
            PartnerDetails.foodPreferences.forEach { food ->
                val chip = Chip(context)
                chip.text = food
                chip.isClickable = true
                chip.isCheckable = true
                chipGroup2.addView(chip)
            }
            PartnerDetails.activityPreferences.forEach { activity ->
                val chip = Chip(context)
                chip.text = activity
                chip.isClickable = true
                chip.isCheckable = true
                chipGroup3.addView(chip)
            }
        }
    }

    /**
     * Gibt eine Liste der ausgewählten Chips in einer Chip-Gruppe zurück.
     *
     * @param chipGroup Die Chip-Gruppe, aus der die ausgewählten Chips abgerufen werden.
     * @return Eine Liste der Texte der ausgewählten Chips.
     */
    fun getSelectedChips(chipGroup: ChipGroup): MutableList<String> {
        val checkedChipIds = chipGroup.checkedChipIds
        val selectedChips = mutableListOf<String>()
        checkedChipIds.forEach { chipId ->
            val chip = chipGroup.findViewById<Chip>(chipId)
            selectedChips.add(chip.text.toString())
        }
        return selectedChips
    }
}