package com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore

import com.example.abschlussprojekt.datamodel.remotemodels.places.PlaceWithPhoto
import java.util.Date

data class CachedPlaces(
    val placesWithPhotos: List<PlaceWithPhoto> = emptyList(),
    val timestamp: Date = Date()
)