package com.example.abschlussprojekt.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussprojekt.data.local.LoveFactDatabase
import com.example.abschlussprojekt.data.local.LoveFacts
import com.example.abschlussprojekt.data.local.NotificationDatabase
import com.example.abschlussprojekt.datamodel.localmodels.LoveFact
import com.example.abschlussprojekt.datamodel.localmodels.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomRepository(
    private var database: LoveFactDatabase,
    private var notificationDatabase: NotificationDatabase
) {

    private val _randomLoveFact = MutableLiveData<LoveFact>()
    val randomLoveFact: LiveData<LoveFact> get() = _randomLoveFact

    private val _notification = MutableLiveData<Notification>()
    val notification: LiveData<Notification> get() = _notification

    /**
     * Füllt die Datenbank mit vordefinierten LoveFacts.
     */
    suspend fun prepopulateLoveFacts() {
        try {
            for (loveFact in LoveFacts.loveFacts) {
                database.loveFactDao.insertLoveFact(loveFact)
            }
        } catch (e: Exception) {
            Log.d("Repository", "Failed to prepopulate Database: $e")
        }
    }

    /**
     * Holt einen zufälligen LoveFact aus der Datenbank.
     */
    fun getRandomLoveFact() {
        try {
            val randomFact = database.loveFactDao.getRandomLoveFact()
            _randomLoveFact.postValue(randomFact)
        } catch (e: Exception) {
            Log.d("Repository", "Failed to get random LoveFact from Database: $e")
        }
    }

    /**
     * Aktualisiert den zufälligen LoveFact im Hintergrund.
     *
     * @param coroutineScope Der CoroutineScope, in dem die Operation ausgeführt wird.
     */
    fun refreshedRandomLoveFact(coroutineScope: CoroutineScope) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val randomFact = database.loveFactDao.getRandomLoveFact()
                withContext(Dispatchers.Main) {
                    _randomLoveFact.postValue(randomFact)
                }
            } catch (e: Exception) {
                Log.d("Repository", "Failed to get random LoveFact from Database: $e")
            }
        }
    }
}