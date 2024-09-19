package com.example.abschlussprojekt.data.remote

import com.example.abschlussprojekt.datamodel.remotemodels.weather.GeocodingResponse
import com.example.abschlussprojekt.datamodel.remotemodels.weather.WeatherResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Die Basis-URL der OpenWeatherMap API.
const val BASE_URL_OPEN_METEO = "https://api.open-meteo.com/v1/"

// Ein HTTP-Logging-Interceptor, um Netzwerk-Anfragen zu protokollieren (für Debugging).
private val logger: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

// Ein OkHttpClient, der den Logging-Interceptor verwendet.
private val httpClient = OkHttpClient.Builder()
    .addInterceptor(logger)
    .build()

// Ein Moshi-Objekt, um JSON-Daten zu parsen und zu serialisieren.
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Ein Retrofit-Objekt, um eine typisierte API zu erstellen.
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL_OPEN_METEO)
    .client(httpClient)
    .build()


// Eine Schnittstelle, die die Methoden für die Interaktion mit der Wetter-API definiert.
interface WeatherApiService {
    // Holt Wetterdaten für einen bestimmten Ort und Zeitraum.
    @GET("forecast")
    suspend fun getWeatherforDate(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,precipitation_sum,precipitation_probability_max",
        @Query("timezone") timezone: String = "Europe/Berlin",
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): WeatherResponse

    // Holt die geographischen Koordinaten für einen Stadtnamen.
    @GET("geocode")
    suspend fun getCoordinates(
        @Query("name") cityName: String,
        @Query("count") count: Int = 1,
        @Query("language") language: String = "de"
    ): GeocodingResponse

}

// Ein Singleton-Objekt, um die Wetter-API-Schnittstelle zu initialisieren.
object WeatherApi {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}