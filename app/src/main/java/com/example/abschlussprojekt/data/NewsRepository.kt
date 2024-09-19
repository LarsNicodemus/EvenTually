package com.example.abschlussprojekt.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussprojekt.data.remote.NewsApi
import com.example.abschlussprojekt.datamodel.remotemodels.news.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsRepository(context: Context) {

    private val _news = MutableLiveData<List<Article>>()
    val news: LiveData<List<Article>>
        get() = _news

    /**
     * Ruft Nachrichten basierend auf der angegebenen Abfrage ab.
     *
     * @param query Die Abfrage, nach der die Nachrichten gefiltert werden sollen. Standardwert ist "Beziehung AND Liebe".
     * @param coroutineScope Der CoroutineScope, in dem die Operation ausgeführt wird.
     */
    fun fetchNews(query: String = "Beziehung AND Liebe", coroutineScope: CoroutineScope) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val response = NewsApi.retrofitService.getNews(query)
                if (response.status == "ok") {
                    withContext(Dispatchers.Main) {
                        _news.postValue(response.articles)
                    }
                } else {
                    Log.d("Repository", "Failed to fetch news: ${response.status}")
                    withContext(Dispatchers.Main) {
                        _news.postValue(emptyList())
                    }
                }
            } catch (e: Exception) {
                Log.d("Repository", "Failed to fetch news: $e")
                withContext(Dispatchers.Main) {
                    _news.postValue(emptyList())
                }
            }
        }
    }
}