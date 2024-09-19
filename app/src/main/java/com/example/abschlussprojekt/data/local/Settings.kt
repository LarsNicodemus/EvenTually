package com.example.abschlussprojekt.data.local

import com.example.abschlussprojekt.R
import com.example.abschlussprojekt.datamodel.localmodels.Setting

object Settings {
    val settings = listOf(
        Setting(
            R.string.settings_profile_title,
            R.drawable.baseline_person_24
        ),
        Setting(
            R.string.settings_partner_title,
            R.drawable.baseline_people_alt_24
        ),
        Setting(
            R.string.settings_events_title,
            R.drawable.baseline_event_24
        ),
        Setting(
            R.string.settings_reminder_title,
            R.drawable.baseline_notifications_24
        ),
        Setting(
            R.string.settings_recs_title,
            R.drawable.featured_seasonal_and_gifts_24dp_000000_fill0_wght400_grad0_opsz24
        ),
        Setting(
            R.string.settings_design_title,
            R.drawable.baseline_design_24
        )
    )
}