package com.example.abschlussprojekt.datamodel.remotemodels.places

import com.squareup.moshi.Json

data class PlacesResponse(
    @Json(name = "results") val results: List<PlaceLocal>
)
