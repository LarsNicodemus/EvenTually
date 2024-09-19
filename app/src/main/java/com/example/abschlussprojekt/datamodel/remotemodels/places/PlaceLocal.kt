package com.example.abschlussprojekt.datamodel.remotemodels.places

import com.squareup.moshi.Json

data class PlaceLocal(
    val name: String,
    val vicinity: String?,
    val photos: List<Photo>? = null,
    @Json(name = "opening_hours") val openingHours: OpeningHours? = null,
    val types: List<String>? = null,
    val rating: Double? = null,
    @Json(name = "price_level") val priceLevel: Int? = null
)