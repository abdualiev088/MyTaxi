package com.example.mytaxi.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userLocation : UserLocation)

    @Query("SELECT * FROM userLocation ORDER BY date ASC LIMIT 1")
    fun getLast(): LiveData<UserLocation>
}