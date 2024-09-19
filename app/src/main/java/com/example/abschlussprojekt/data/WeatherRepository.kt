package com.example.abschlussprojekt.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussprojekt.data.remote.WeatherApi
import com.example.abschlussprojekt.datamodel.remotemodels.weather.WeatherResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherRepository {
    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> = _weatherData

    /**
     * Ruft Wetterdaten für die angegebenen Koordinaten und das Datum ab.
     *
     * @param latitude Die geografische Breite.
     * @param longitude Die geografische Länge.
     * @param date Das Datum für das die Wetterdaten abgerufen werden sollen.
     */
    fun fetchWeatherData(latitude: Double, longitude: Double, date: String) {
        val daily = "temperature_2m_max,temperature_2m_min,precipitation_sum,precipitation_probability_max"
        val timezone = "Europe/Berlin"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = WeatherApi.retrofitService.getWeatherforDate(
                    latitude,
                    longitude,
                    daily,
                    timezone,
                    date,
                    date
                )

                withContext(Dispatchers.Main) {
                    _weatherData.postValue(response)
                }
            } catch (e: Exception) {
                Log.e("WeatherRepository", "Error fetching weather data: $e")
            }
        }
    }
}