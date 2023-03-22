package com.example.mytaxi.Room.MVVM

import androidx.lifecycle.LiveData
import com.example.mytaxi.Room.LocationDao
import com.example.mytaxi.Room.UserLocation

class UserRepository (private val locationDao: LocationDao){
    val getLast: LiveData<UserLocation> = locationDao.getLast()

    suspend fun insert(userLocation: UserLocation){
        locationDao.insert(userLocation)
    }
}