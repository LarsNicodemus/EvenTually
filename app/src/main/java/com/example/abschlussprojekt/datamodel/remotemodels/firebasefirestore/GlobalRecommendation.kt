package com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore

data class GlobalRecommendation(
    val giftCardSelected: Boolean = false,
    val presentSelected: Boolean = false,
    val restaurantSelected: Boolean = false,
    val activitySelected: Boolean = false,
    val distance: Int = 0
)
