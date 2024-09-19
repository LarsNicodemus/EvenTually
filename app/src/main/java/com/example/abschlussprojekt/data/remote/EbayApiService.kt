package com.example.abschlussprojekt.data.remote

import android.util.Base64
import android.util.Log
import com.example.abschlussprojekt.BuildConfig
import com.example.abschlussprojekt.datamodel.remotemodels.shopping.SearchResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.io.IOException


const val BASE_URL_EBAY = "https://api.ebay.com"


private const val authorization = BuildConfig.EBAY_AUTHORIZATION

class EbayTokenManager {
    private var accessToken: String? = null
    private var expirationTime: Long = 0

    suspend fun getAccessToken(): String {
        if (accessToken == null || System.currentTimeMillis() >= expirationTime) {
            refreshAccessToken()
        }
        return accessToken!!
    }

    private suspend fun refreshAccessToken() {
        try {
            val client = OkHttpClient()
            val requestBody = FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("scope", "https://api.ebay.com/oauth/api_scope")
                .build()

            val request = Request.Builder()
                .url("https://api.ebay.com/identity/v1/oauth2/token")
                .post(requestBody)
                .header(
                    "Authorization", "Basic " + Base64.encodeToString(
                        authorization.toByteArray(),
                        Base64.NO_WRAP
                    )
                )
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build()

            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }

            if (response.isSuccessful) {
                val jsonResponse = JSONObject(response.body?.string() ?: "")
                accessToken = jsonResponse.getString("access_token")
                val expiresIn = jsonResponse.getInt("expires_in")
                expirationTime = System.currentTimeMillis() + (expiresIn * 1000)
            } else {
                val errorBody = response.body?.string() ?: "No error body"
                throw IOException("Unexpected code ${response.code}: $errorBody")
            }
        } catch (e: Exception) {
            Log.e("EbayTokenManager", "Error refreshing token", e)
            throw Exception("Failed to refresh token: ${e.message}", e)
        }
    }
}

private val logger: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val tokenManager = EbayTokenManager()

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(logger)
    .addInterceptor { chain ->
        val original = chain.request()
        val accessToken = runBlocking { tokenManager.getAccessToken() }
        val request = original.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
        chain.proceed(request)
    }
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL_EBAY)
    .client(httpClient)
    .build()

interface EbayApiService {
    @GET("buy/browse/v1/item_summary/search")
    suspend fun searchItems(
        @Header("X-EBAY-C-MARKETPLACE-ID") marketplaceId: String = "EBAY_DE",
        @Header("Content-Language") contentLanguage: String = "de-DE",
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
        @Query("fieldgroups") fieldgroups: String = "MATCHING_ITEMS"
    ): SearchResponse
}

object EbayApi {
    val retrofitService: EbayApiService by lazy {
        retrofit.create(EbayApiService::class.java)
    }
}