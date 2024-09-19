package com.example.abschlussprojekt.datamodel.remotemodels.geocoding

import com.squareup.moshi.Json

data class AddressComponent(
    @Json(name = "long_name") val longName: String,
    @Json(name = "short_name") val shortName: String,
    @Json(name = "types") val types: List<String>
)
