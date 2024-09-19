package com.example.abschlussprojekt.datamodel.remotemodels.geocoding

import com.squareup.moshi.Json

data class GeocodeResponse(
    @Json(name = "results") val results: List<GeocodeResult>,
    @Json(name = "status") val status: String
)
