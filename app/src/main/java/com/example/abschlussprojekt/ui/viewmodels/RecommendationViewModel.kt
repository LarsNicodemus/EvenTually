package com.example.abschlussprojekt.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.abschlussprojekt.data.EbayRepository
import com.example.abschlussprojekt.data.FirebaseRepository
import com.example.abschlussprojekt.data.GoogleMapsRepository
import com.example.abschlussprojekt.data.local.PartnerDetails
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalRecommendation
import com.example.abschlussprojekt.datamodel.remotemodels.places.PlaceWithPhoto
import com.example.abschlussprojekt.datamodel.remotemodels.shopping.ItemSummary
import com.example.abschlussprojekt.datamodel.remotemodels.shopping.SearchResponse
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.launch
import java.util.Calendar

class RecommendationViewModel(application: Application) : AndroidViewModel(application) {
    private val ebayRepository = EbayRepository()
    private val googleMapsRepository = GoogleMapsRepository(application)
    private val firebaseRepository = FirebaseRepository()

    var recsRef: DocumentReference? = firebaseRepository.recsRef

    val cardSuggestions = ebayRepository.cardSuggestions
    val giftResultSuggestion: LiveData<SearchResponse> = ebayRepository.giftSuggestions
    val restaurantResults = googleMapsRepository.selectedRestaurants
    val activitiesResults = googleMapsRepository.selectedActivities
    val cityName = googleMapsRepository.cityNameString
    val recommendationData = firebaseRepository.recsData

    var place: PlaceWithPhoto? = null
    var product: ItemSummary? = null

    /**
     * Aktualisiert die Empfehlungen in Firebase.
     * @param recs Die neuen Empfehlungen.
     */
    fun firebaseUpdateRecs(recs: GlobalRecommendation) {
        firebaseRepository.firebaseUpdateRecs(recs)
    }

    /**
     * Aktualisiert ein bestimmtes Feld der Empfehlungen in Firebase.
     * @param field Das zu aktualisierende Feld.
     * @param value Der neue Wert des Feldes.
     */
    fun firebaseUpdatePartialRecs(field: String, value: Any) {
        firebaseRepository.firebaseUpdatePartialRecs(field, value)
    }

    /**
     * Holt die Empfehlungsdaten aus Firebase.
     */
    fun fetchRecsData() {
        firebaseRepository.fetchRecsData()
    }

    /**
     * Sucht nach Grußkarten basierend auf den Präferenzen.
     * @param preferences Die Präferenzen für die Suche.
     */
    fun searchCards(preferences: List<String>) {
        viewModelScope.launch {
            ebayRepository.searchCards(preferences)
        }
    }

    /**
     * Sucht nach Geschenken basierend auf den Präferenzen.
     * @param preferences Die Präferenzen für die Suche.
     */
    fun getGiftSuggestions(preferences: List<String>) {
        try {
            viewModelScope.launch {
                ebayRepository.getGiftSuggestionTwo(preferences)
                Log.d("MainViewModel", "Gift Suggestions erfolgreich geladen")
            }
        } catch (e: Exception) {
            Log.e("MainViewModel", "Fehler beim Laden der Gift Suggestions", e)
        }
    }

    /**
     * Sucht nach Restaurants basierend auf den Präferenzen.
     * @param foodPreferences Die Präferenzen für die Restaurantsuche.
     */
    fun searchRestaurantWithPhoto(foodPreferences: List<String>) {
        viewModelScope.launch {
            googleMapsRepository.searchRestaurantsWithPhoto(
                foodPreferences,
                firebaseRepository.profileData.value?.location,
                firebaseRepository.recsData.value?.distance
            )
        }
    }

    /**
     * Sucht nach Aktivitäten basierend auf den Präferenzen.
     * @param activityPreferences Die Präferenzen für die Aktivitätensuche.
     */
    fun searchActivitiesWithPhoto(activityPreferences: List<String>) {
        viewModelScope.launch {
            googleMapsRepository.searchActivitiesWithPhoto(
                activityPreferences,
                firebaseRepository.profileData.value?.location,
                firebaseRepository.recsData.value?.distance
            )
        }
    }

    /**
     * Gibt den Text für ein Restaurant basierend auf den Präferenzen zurück.
     * @param place Das Restaurant.
     * @return Der Text für das Restaurant.
     */
    fun getTextForRestaurant(place: Place): String {
        return place.placeTypes?.mapNotNull { placeType ->
            PartnerDetails.foodPreferencesMap.entries.find { it.value == placeType }?.key
        }?.joinToString(", ") ?: "keine Informationen"
    }

    /**
     * Gibt den Text für einen Ort basierend auf den Präferenzen zurück.
     * @param place Der Ort.
     * @return Der Text für den Ort.
     */
    fun getTextForPlace(place: Place): String {
        return place.placeTypes?.mapNotNull { placeType ->
            PartnerDetails.activityPreferencesMap.entries.find { it.value == placeType }?.key
        }?.joinToString(", ") ?: "keine Informationen"
    }

    /**
     * Überprüft, ob ein Restaurant am aktuellen Wochentag geöffnet ist.
     * @param place Das Restaurant.
     * @return True, wenn das Restaurant geöffnet ist, sonst False.
     */
    fun isRestaurantOpen(place: Place): Boolean {
        val openingHours = place.openingHours ?: return false
        val periods = openingHours.periods ?: return false
        val now = Calendar.getInstance()
        val currentDay = (now.get(Calendar.DAY_OF_WEEK) + 5) % 7
        val period = periods.find { it.open?.day?.ordinal == currentDay } ?: return false
        val openTime = period.open?.time
        val closeTime = period.close?.time
        if (openTime == null || closeTime == null) {
            return false
        }
        val currentTimeMinutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)
        val openTimeMinutes = openTime.hours * 60 + openTime.minutes
        val closeTimeMinutes = closeTime.hours * 60 + closeTime.minutes
        return if (openTimeMinutes < closeTimeMinutes) {
            currentTimeMinutes in openTimeMinutes until closeTimeMinutes
        } else {
            currentTimeMinutes >= openTimeMinutes || currentTimeMinutes < closeTimeMinutes
        }
    }

    /**
     * Holt den Städtenamen basierend auf den Koordinaten.
     * @param location Die Koordinaten.
     */
    fun getCityName(location: LatLng) {
        viewModelScope.launch {
            googleMapsRepository.fetchCityName(location)
        }
    }
}