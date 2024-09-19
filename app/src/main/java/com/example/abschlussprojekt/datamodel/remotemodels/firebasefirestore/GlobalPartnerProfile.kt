package com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore

data class GlobalPartnerProfile(
    val name: String = "",
    val birthDate: String = "",
    val age: Int = 0,
    val gender: String = "",
    val imageString: String = "",
    val likes: MutableList<String> = mutableListOf(),
    val foodPreferences: MutableList<String> = mutableListOf(),
    val activityPreferences: MutableList<String> = mutableListOf()
)
