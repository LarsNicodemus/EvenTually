package com.example.abschlussprojekt.datamodel.remotemodels.places

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.google.android.libraries.places.api.model.Place


data class PlaceWithPhoto(
    val place: Place?,
    val photo: Bitmap? = null
)