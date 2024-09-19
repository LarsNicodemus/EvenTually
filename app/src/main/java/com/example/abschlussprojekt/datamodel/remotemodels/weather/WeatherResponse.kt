package com.example.abschlussprojekt.datamodel.remotemodels.weather

import com.squareup.moshi.Json

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    @Json(name = "generationtime_ms") val generationTimeMs: Double,
    @Json(name = "utc_offset_seconds") val utcOffsetSeconds: Int,
    val timezone: String,
    @Json(name = "timezone_abbreviation") val timeZoneAbbreviation: String,
    val elevation: Double,
    @Json(name = "daily_units") val dailyUnits: DailyUnits,
    val daily: Daily
)
