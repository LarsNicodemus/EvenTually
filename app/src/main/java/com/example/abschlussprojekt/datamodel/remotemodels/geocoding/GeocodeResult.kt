package com.example.abschlussprojekt.datamodel.remotemodels.geocoding

import com.squareup.moshi.Json

data class GeocodeResult(
    @Json(name = "address_components") val addressComponents: List<AddressComponent>,
    @Json(name = "formatted_address") val formattedAddress: String
)
