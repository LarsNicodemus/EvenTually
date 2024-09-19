package com.example.abschlussprojekt.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussprojekt.BuildConfig
import com.example.abschlussprojekt.data.local.PartnerDetails
import com.example.abschlussprojekt.data.remote.GoogleApi
import com.example.abschlussprojekt.datamodel.remotemodels.places.PlaceWithPhoto
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class GoogleMapsRepository(context: Context) {
    private val googleKey = BuildConfig.GOOGLE_API_KEY_SEC

    private val _selectedRestaurants = MutableLiveData<List<PlaceWithPhoto>>()
    val selectedRestaurants: LiveData<List<PlaceWithPhoto>> get() = _selectedRestaurants

    private val _selectedActivities = MutableLiveData<List<PlaceWithPhoto>>()
    val selectedActivities: LiveData<List<PlaceWithPhoto>> get() = _selectedActivities

    private val _cityNameString = MutableLiveData<String?>()
    val cityNameString: LiveData<String?> get() = _cityNameString

    init {
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(context, googleKey)
        }
    }

    private val placesClient: PlacesClient = Places.createClient(context)

    /**
     * Sucht nach Orten mit Fotos basierend auf den Benutzerpräferenzen und dem Standort.
     *
     * @param preferences Liste der Benutzerpräferenzen.
     * @param typeMap Karte der Präferenztypen zu den Ortstypen.
     * @param liveData LiveData, um die Ergebnisse zu posten.
     * @param distanceData Suchradius in Kilometern.
     * @param locationData Aktueller Standort des Benutzers.
     */
    private fun searchPlacesWithPhoto(
        preferences: List<String>,
        typeMap: Map<String, String>,
        liveData: MutableLiveData<List<PlaceWithPhoto>>,
        distanceData: Int,
        locationData: GeoPoint
    ) {
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.RATING,
            Place.Field.PHOTO_METADATAS,
            Place.Field.OPENING_HOURS,
            Place.Field.TYPES,
            Place.Field.WEBSITE_URI
        )
        val radius = (distanceData * 1000).toDouble()
        val location = LatLng(locationData.latitude, locationData.longitude)
        val circle = CircularBounds.newInstance(location, radius)
        val includedTypes = preferences.mapNotNull { typeMap[it] }

        val searchNearbyRequest = SearchNearbyRequest.builder(circle, placeFields)
            .setIncludedTypes(includedTypes)
            .setMaxResultCount(10)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = placesClient.searchNearby(searchNearbyRequest).await()
                val results = response.places
                val loadedPlacesWithPhotos = mutableListOf<PlaceWithPhoto>()

                // Paralleles Laden von Fotos mit Coroutines
                val photoRequests = results.map { place ->
                    async {
                        loadPhotoForPlace(place)
                    }
                }

                // Warten auf alle parallelen Fotoanfragen
                loadedPlacesWithPhotos.addAll(photoRequests.awaitAll())

                // Setzen der LiveData-Ergebnisse auf dem Hauptthread
                withContext(Dispatchers.Main) {
                    liveData.postValue(loadedPlacesWithPhotos)
                }
            } catch (exception: Exception) {
                Log.e("Repository", "Error searching places: $exception")
            }
        }
    }

    /**
     * Lädt ein Foto für einen gegebenen Ort.
     *
     * @param place Der Ort, für den das Foto geladen werden soll.
     * @return Ein PlaceWithPhoto-Objekt, das den Ort und sein Foto enthält.
     */
    private suspend fun loadPhotoForPlace(place: Place): PlaceWithPhoto {
        return withContext(Dispatchers.IO) {
            val photoMetadatas = place.photoMetadatas
            if (photoMetadatas != null && photoMetadatas.isNotEmpty()) {
                val photoMetadata = photoMetadatas[0]
                val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500)
                    .setMaxHeight(500)
                    .build()

                try {
                    val fetchPhotoResponse = placesClient.fetchPhoto(photoRequest).await()
                    val bitmap = fetchPhotoResponse.bitmap
                    PlaceWithPhoto(
                        place,
                        bitmap
                    )
                } catch (e: Exception) {
                    Log.e("Repository", "Error loading photo for place ${place.name}: $e")
                    PlaceWithPhoto(
                        place,
                        null
                    )
                }
            } else {
                PlaceWithPhoto(
                    place,
                    null
                )
            }
        }
    }

    /**
     * Sucht nach Restaurants mit Fotos basierend auf den Benutzerpräferenzen und dem Standort.
     *
     * @param foodPreferences Liste der Essenspräferenzen.
     * @param location Aktueller Standort des Benutzers.
     * @param distance Suchradius in Kilometern.
     */
    fun searchRestaurantsWithPhoto(
        foodPreferences: List<String>,
        location: GeoPoint?,
        distance: Int?
    ) {
        val loc = location ?: GeoPoint(50.3830, 8.0493)
        val dist = distance ?: 5
        searchPlacesWithPhoto(
            foodPreferences,
            PartnerDetails.foodPreferencesMap,
            _selectedRestaurants,
            dist,
            loc
        )
        Log.d("Repository", "searchRestaurantsWithPhoto: $loc $dist")
    }

    /**
     * Sucht nach Aktivitäten mit Fotos basierend auf den Benutzerpräferenzen und dem Standort.
     *
     * @param activityPreferences Liste der Aktivitätspräferenzen.
     * @param location Aktueller Standort des Benutzers.
     * @param distance Suchradius in Kilometern.
     */
    fun searchActivitiesWithPhoto(
        activityPreferences: List<String>,
        location: GeoPoint?,
        distance: Int?
    ) {
        val loc = location ?: GeoPoint(50.3830, 8.0493)
        val dist = distance ?: 5
        searchPlacesWithPhoto(
            activityPreferences,
            PartnerDetails.activityPreferencesMap,
            _selectedActivities,
            dist,
            loc
        )
        Log.d("Repository", "searchActivitiesWithPhoto: $loc $dist")
    }

    /**
     * Ruft den Stadtnamen basierend auf dem gegebenen Standort ab.
     *
     * @param location Der Standort, für den der Stadtname abgerufen werden soll.
     */
    suspend fun fetchCityName(location: LatLng) {
        try {
            val response = GoogleApi.retrofitService.getCityName(
                "${location.latitude},${location.longitude}",
                googleKey
            )

            response.results.find { geoCodeResult ->
                geoCodeResult.addressComponents.find {
                    it.types.contains("locality")
                } != null
            }?.let { result ->
                _cityNameString.value =
                    result.addressComponents.find { it.types.contains("locality") }?.longName
            }
            Log.d("Repository", "Response: ${_cityNameString.value}")
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching city name: $e")
        }
    }
}