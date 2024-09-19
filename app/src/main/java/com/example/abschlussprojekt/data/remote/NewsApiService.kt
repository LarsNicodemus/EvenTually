package com.example.abschlussprojekt.data.remote

import com.example.abschlussprojekt.BuildConfig
import com.example.abschlussprojekt.datamodel.remotemodels.news.NewsResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// URL zur NewsAPI-Org API
private const val BASE_URL_NEWS = "https://newsapi.org/v2/"

// Deine API-Schlüssel
private const val newsApiKey = BuildConfig.NEWS_API_KEY

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
    .baseUrl(BASE_URL_NEWS)
    .client(httpClient)
    .build()

interface NewsApiService {
    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("language") language: String = "de",
        @Query("apiKey") apiKey: String = newsApiKey
    ): NewsResponse
}

object NewsApi {
    val retrofitService: NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }
}