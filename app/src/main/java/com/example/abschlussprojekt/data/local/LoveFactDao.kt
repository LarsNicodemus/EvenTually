package com.example.abschlussprojekt.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.abschlussprojekt.datamodel.localmodels.LoveFact

@Dao
interface LoveFactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoveFact(loveFact: LoveFact)

    @Update
    suspend fun updateLoveFact(loveFact: LoveFact)

    @Query("SELECT * FROM love_facts")
    fun getAllLoveFacts(): LiveData<List<LoveFact>>

    @Query("SELECT * FROM love_facts WHERE id = :id")
    fun getLoveFactById(id: Long): LiveData<LoveFact>

    @Query("SELECT * FROM love_facts ORDER BY RANDOM() LIMIT 1")
    fun getRandomLoveFact(): LoveFact

    @Query("DELETE FROM love_facts WHERE id = :id")
    suspend fun deleteLoveFactById(id: Long)

    @Query("DELETE FROM love_facts")
    suspend fun deleteAllLoveFacts()

    @Query("SELECT COUNT(*) FROM love_facts")
    fun getLoveFactCount(): Int
}