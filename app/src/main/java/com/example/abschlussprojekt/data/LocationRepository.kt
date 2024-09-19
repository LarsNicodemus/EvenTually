package com.example.abschlussprojekt.data

import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LocationRepository(context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    /**
     * Erstellt eine Anforderung für Standortaktualisierungen.
     */
    fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(5 * 60 * 1000) // 5 Minuten in Millisekunden
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateIntervalMillis(2 * 60 * 1000) // Mindestens 2 Minuten zwischen Updates
            .setMaxUpdateDelayMillis(10 * 60 * 1000) // Maximal 10 Minuten Verzögerung
            .build()
    }

    /**
     * Fordert eine einmalige Standortaktualisierung an und aktualisiert das Profil.
     *
     * @param profileRef Die Referenz zum Profil-Dokument in Firestore.
     * @param coroutineScope Der CoroutineScope, in dem die Operation ausgeführt wird.
     */
    fun requestSingleUpdate(profileRef: DocumentReference, coroutineScope: CoroutineScope) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val location = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
                location?.let {
                    profileRef.update("location", GeoPoint(it.latitude, it.longitude))
                    withContext(Dispatchers.Main) {
                        _location.value = it
                    }
                }
            } catch (e: SecurityException) {
                Log.d("LocationRepository", "No location permission granted yet: $e")
            } catch (e: Exception) {
                Log.d("LocationRepository", "Failed to get current location: $e")
            }
        }
    }

    /**
     * Erstellt einen Callback für Standortaktualisierungen.
     *
     * @param profileRef Die Referenz zum Profil-Dokument in Firestore.
     */
    fun createLocationCallback(profileRef: DocumentReference) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    _location.value = location
                    profileRef.update("location", GeoPoint(location.latitude, location.longitude))
                    Log.d("LocationRepository", "Location updated: $location")
                }
            }
        }
    }

    /**
     * Startet die Standortaktualisierungen.
     *
     * @param coroutineScope Der CoroutineScope, in dem die Operation ausgeführt wird.
     */
    fun startLocationUpdates(coroutineScope: CoroutineScope) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } catch (e: SecurityException) {
                Log.d("LocationRepository", "No location permission granted yet: $e")
            } catch (e: Exception) {
                Log.d("LocationRepository", "Failed to start location updates: $e")
            }
        }
    }

    /**
     * Stoppt die Standortaktualisierungen.
     *
     * @param coroutineScope Der CoroutineScope, in dem die Operation ausgeführt wird.
     */
    fun stopLocationUpdates(coroutineScope: CoroutineScope) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            } catch (e: Exception) {
                Log.d("LocationRepository", "Failed to stop location updates: $e")
            }
        }
    }
}