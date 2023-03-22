package com.example.mytaxi.Room.MVVM

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytaxi.Room.UserDatabase
import com.example.mytaxi.Room.UserLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserViewModel(application: Application): AndroidViewModel(application) {
    private var repository : UserRepository
    var getLast : LiveData<UserLocation>
    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        getLast = repository.getLast
    }
    fun insert(userLocation: UserLocation) {
        viewModelScope.launch {
            repository.insert(userLocation)
        }
    }
}