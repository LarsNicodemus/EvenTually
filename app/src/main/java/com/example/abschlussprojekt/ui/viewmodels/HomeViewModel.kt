package com.example.abschlussprojekt.ui.viewmodels

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.abschlussprojekt.data.FirebaseRepository
import com.example.abschlussprojekt.data.LocationRepository
import com.example.abschlussprojekt.data.NewsRepository
import com.example.abschlussprojekt.data.RoomRepository
import com.example.abschlussprojekt.data.WeatherRepository
import com.example.abschlussprojekt.data.local.getDatabase
import com.example.abschlussprojekt.data.local.getNotificationDatabase
import com.example.abschlussprojekt.datamodel.localmodels.LoveFact
import com.example.abschlussprojekt.datamodel.remotemodels.weather.WeatherResponse
import kotlinx.coroutines.launch

/**
 * ViewModel zur Verwaltung der Startseite der App.
 * @param application Die Anwendung, die dieses ViewModel verwendet.
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val loveFactDatabase = getDatabase(application)
    private val notificationDatabase = getNotificationDatabase(application)
    private val firebaseRepository = FirebaseRepository()
    private val roomRepository = RoomRepository(loveFactDatabase, notificationDatabase)
    private val newsRepository = NewsRepository(application)
    private val weatherRepository = WeatherRepository()
    private val locationRepository = LocationRepository(application)
    private val profilRef = firebaseRepository.profileRef
    val randomLoveFact: LiveData<LoveFact> = roomRepository.randomLoveFact
    val news = newsRepository.news
    val profileData = firebaseRepository.profileData
    val weatherData: LiveData<WeatherResponse> = weatherRepository.weatherData

    init {
        prepopulateLoveFacts()
        createLocationRequest()
        createLocationCallback()
    }

    val location: LiveData<Location> = locationRepository.location

    /**
     * Startet die Standortaktualisierungen.
     */
    fun startLocationUpdates() {
        locationRepository.startLocationUpdates(viewModelScope)
    }

    /**
     * Fordert eine einmalige Standortaktualisierung an.
     */
    fun requestSingleUpdate() {
        if (profilRef != null) {
            locationRepository.requestSingleUpdate(profilRef, viewModelScope)
        }
    }

    /**
     * Stoppt die Standortaktualisierungen.
     */
    fun stopLocationUpdates() {
        locationRepository.stopLocationUpdates(viewModelScope)
    }

    /**
     * Erstellt die Standortanforderung.
     */
    private fun createLocationRequest() {
        locationRepository.createLocationRequest()
        Log.d("HomeViewModel", "Location request created")
    }

    /**
     * Erstellt den Standort-Callback.
     */
    private fun createLocationCallback() {
        if (profilRef != null) {
            locationRepository.createLocationCallback(profilRef)
        }
        Log.d("HomeViewModel", "Location callback created")
    }

    /**
     * Holt einen neuen zufälligen Liebesfakt.
     */
    fun fetchNewRandomLoveFact() {
        roomRepository.refreshedRandomLoveFact(viewModelScope)
    }

    /**
     * Befüllt die LoveFact-Datenbank.
     */
    private fun prepopulateLoveFacts() {
        viewModelScope.launch {
            roomRepository.prepopulateLoveFacts()
        }
    }

    /**
     * Ruft Nachrichten über die News API ab.
     */
    fun getNews() {
        newsRepository.fetchNews("Beziehung AND Liebe", viewModelScope)
    }

    /**
     * Ruft Wetterdaten über die OpenMeteo API ab.
     */
    fun loadWeatherData(latitude: Double, longitude: Double, date: String) {
        viewModelScope.launch {
            weatherRepository.fetchWeatherData(latitude, longitude, date)
        }
    }
}