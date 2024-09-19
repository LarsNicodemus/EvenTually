package com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore

import com.google.firebase.firestore.GeoPoint

data class GlobalProfile(
    val name: String = "",
    val birthDate: String = "",
    val age: Int = 0,
    val gender: String = "",
    val imageString: String = "",
    val location: GeoPoint? = null,
    val token: String = ""
)
