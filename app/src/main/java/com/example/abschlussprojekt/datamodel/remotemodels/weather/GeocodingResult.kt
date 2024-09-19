package com.example.abschlussprojekt.datamodel.remotemodels.weather

data class GeocodingResult(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val admin1: String?
)
