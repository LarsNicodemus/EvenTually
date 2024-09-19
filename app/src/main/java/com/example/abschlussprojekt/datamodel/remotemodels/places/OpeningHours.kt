package com.example.abschlussprojekt.datamodel.remotemodels.places

import com.squareup.moshi.Json

data class OpeningHours(
    @Json(name = "open_now") val openNow: Boolean
)
