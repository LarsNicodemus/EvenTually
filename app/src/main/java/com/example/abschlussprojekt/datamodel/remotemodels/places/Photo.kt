package com.example.abschlussprojekt.datamodel.remotemodels.places

import com.squareup.moshi.Json

data class Photo(
    @Json(name = "height") val height: Int,
    @Json(name = "width") val width: Int,
    @Json(name = "photo_reference") val photoReference: String,
    @Json(name = "html_attributions") val htmlAttributions: List<String>?
)
