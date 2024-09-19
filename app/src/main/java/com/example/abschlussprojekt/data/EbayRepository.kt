package com.example.abschlussprojekt.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussprojekt.data.local.PartnerDetails
import com.example.abschlussprojekt.data.remote.EbayApi
import com.example.abschlussprojekt.datamodel.remotemodels.shopping.ItemSummary
import com.example.abschlussprojekt.datamodel.remotemodels.shopping.SearchResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class EbayRepository {

    private val _giftSuggestions = MutableLiveData<SearchResponse>()
    val giftSuggestions: LiveData<SearchResponse> = _giftSuggestions

    private val _cardSuggestions = MutableLiveData<SearchResponse>()
    val cardSuggestions: LiveData<SearchResponse> = _cardSuggestions

    /**
     * Sucht nach Artikeln basierend auf der angegebenen Abfrage und gibt eine einzelne Antwort zurück.
     *
     * @param query Die Abfrage, nach der die Artikel gesucht werden sollen.
     * @param limit Die maximale Anzahl der zurückgegebenen Artikel. Standardwert ist 10.
     * @return Eine `SearchResponse`-Instanz mit den Suchergebnissen.
     */
    private suspend fun searchItemsWithReturnOne(query: String, limit: Int = 10): SearchResponse {
        val marketplaceId = "EBAY_DE"
        val contentLanguage = "de-DE"
        return try {
            val response = EbayApi.retrofitService.searchItems(
                query = query,
                marketplaceId = marketplaceId,
                contentLanguage = contentLanguage,
                limit = limit
            )
            response
        } catch (e: Exception) {
            Log.e("EbayRepository", "Failed to search items: $e")
            SearchResponse(emptyList())
        }
    }

    /**
     * Sucht nach Karten basierend auf den angegebenen Präferenzen.
     *
     * @param preferences Die Liste der Präferenzen.
     * @param limit Die maximale Anzahl der zurückgegebenen Artikel. Standardwert ist 10.
     */
    fun searchCards(preferences: List<String>, limit: Int = 10) {
        CoroutineScope(Dispatchers.IO).launch {
            val colors = mutableListOf<String>()
            val noColors = listOf("Karte Jahrestag", "Karte Liebe", "Anniversary Card")

            preferences.forEach { preference ->
                if (preference in PartnerDetails.colors) {
                    colors.addAll(
                        listOf(
                            "Karte Jahrestag $preference",
                            "Karte Liebe $preference",
                            "Anniversary Card $preference"
                        )
                    )
                }
            }

            val queries = colors.ifEmpty { noColors }

            val deferredResults = queries.map { query ->
                async { searchItemsWithReturnOne(query, limit) }
            }

            val results = deferredResults.awaitAll()
            val combinedResults = combinedResultsShopping(results)
            _cardSuggestions.postValue(combinedResults)
        }
    }


    /**
     * Sucht nach Artikeln basierend auf der angegebenen Abfrage und gibt eine Liste von Antworten zurück.
     *
     * @param query Die Abfrage, nach der die Artikel gesucht werden sollen.
     * @param limit Die maximale Anzahl der zurückgegebenen Artikel. Standardwert ist 10.
     * @return Eine Liste von `SearchResponse`-Instanzen mit den Suchergebnissen.
     */
    private suspend fun searchItemsWithReturn(
        query: String,
        limit: Int = 10
    ): List<SearchResponse> {
        val marketplaceId = "EBAY_DE"
        val contentLanguage = "de-DE"
        return try {
            val response = EbayApi.retrofitService.searchItems(
                query = query,
                marketplaceId = marketplaceId,
                contentLanguage = contentLanguage,
                limit = limit
            )
            listOf(response)
        } catch (e: Exception) {
            Log.e("EbayRepository", "Failed to search items: $e")
            emptyList()
        }
    }

    /**
     * Ruft Geschenkempfehlungen basierend auf den angegebenen Präferenzen ab.
     *
     * @param preferences Die Liste der Präferenzen.
     */
    fun getGiftSuggestionTwo(preferences: List<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            val deferredResults = preferences.map { preference ->
                async { searchItemsWithReturn(preference) }
            }

            val results = deferredResults.awaitAll().flatten()
            val combinedResults = combinedResultsShopping(results)
            _giftSuggestions.postValue(combinedResults)
        }
    }

    /**
     * Kombiniert die Suchergebnisse zu einer einzigen `SearchResponse`.
     *
     * @param results Die Liste der `SearchResponse`-Instanzen.
     * @return Eine kombinierte `SearchResponse`-Instanz.
     */
    private fun combinedResultsShopping(results: List<SearchResponse>): SearchResponse {
        val uniqueItems = mutableSetOf<ItemSummary>()
        results.forEach { response ->
            uniqueItems.addAll(response.itemSummaries)
        }
        return SearchResponse(uniqueItems.toList())
    }
}