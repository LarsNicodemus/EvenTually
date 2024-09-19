package com.example.abschlussprojekt.datamodel.remotemodels.weather

import com.squareup.moshi.Json

data class DailyUnits(
    val time: String,
    @Json(name = "temperature_2m_max") val temperatureMax: String,
    @Json(name = "temperature_2m_min") val temperatureMin: String,
    @Json(name = "precipitation_sum") val precipitationSum: String,
    @Json(name = "precipitation_probability_max") val precipitationProbabilityMax: String
)
