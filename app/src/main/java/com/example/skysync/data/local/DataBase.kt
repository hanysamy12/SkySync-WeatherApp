package com.example.skysync.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.skysync.models.StoredLocation

@Database(entities = [StoredLocation::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun getLocationDao(): LocationsDAO

    companion object {
        private var INSTANCE: DataBase? = null
        fun getInstance(context: Context): DataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, DataBase::class.java, "location_dp"
                ).build()
                INSTANCE = instance
                instance
            }

        }
    }
}