package com.example.abschlussprojekt.data.remote

import com.example.abschlussprojekt.datamodel.remotemodels.geocoding.GeocodeResponse
import com.example.abschlussprojekt.datamodel.remotemodels.places.PlacesResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL_GOOGLE = "https://maps.googleapis.com/maps/api/"

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
    .baseUrl(BASE_URL_GOOGLE)
    .client(httpClient)
    .build()

interface GoogleApiService {
    @GET("place/nearbysearch/json")
    suspend fun getResponseSimple(
        @Query("location") location: String = "50.387631,8.06362",
        @Query("keyword") keyword: String,
        @Query("radius") radius: Int = 1000,
        @Query("key") apiKey: String
    ): PlacesResponse

    @GET("geocode/json")

    suspend fun getCityName(
        @Query("latlng") latlng: String,
        @Query("key") apiKey: String
    ): GeocodeResponse
}

object GoogleApi {
    val retrofitService: GoogleApiService by lazy {
        retrofit.create(GoogleApiService::class.java)
    }
}