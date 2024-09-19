package com.example.abschlussprojekt.datamodel.remotemodels.weather

import com.squareup.moshi.Json

data class Daily(
    val time: List<String>,
    @Json(name = "temperature_2m_max") val temperatureMax: List<Double>,
    @Json(name = "temperature_2m_min") val temperatureMin: List<Double>,
    @Json(name = "precipitation_sum") val precipitationSum: List<Double>,
    @Json(name = "precipitation_probability_max") val precipitationProbabilityMax: List<Int>
)
