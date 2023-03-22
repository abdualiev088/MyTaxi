package com.example.mytaxi.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mytaxi.User

@Database(entities = [UserLocation::class], version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao() : LocationDao
    //    Everything  within companion object will be basically visible to other classes
    companion object{
        //        UserDatabase singletone class -> Our StudentDatabase will have only one instance of its class
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java, "userLocation"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}