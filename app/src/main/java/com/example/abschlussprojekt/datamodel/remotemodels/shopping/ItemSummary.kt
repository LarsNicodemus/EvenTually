package com.example.abschlussprojekt.datamodel.remotemodels.shopping

import android.os.Parcel
import android.os.Parcelable


data class ItemSummary(
    val itemId: String,
    val title: String,
    val price: Price,
    val image: Image? = null,
    val itemWebUrl: String
)
