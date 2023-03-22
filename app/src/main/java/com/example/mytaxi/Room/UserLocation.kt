package com.example.mytaxi.Room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mapbox.mapboxsdk.geometry.LatLng
import java.time.LocalDateTime
import java.util.Date

@Entity(tableName = "userLocation")
data class UserLocation (
    @PrimaryKey val id : Int,
    val location : String,
    val date : String
    )