package com.example.skysync.data.local

import android.content.Context
import com.example.skysync.models.StoredLocation
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImp private constructor(private val dao: LocationsDAO):
    WeatherLocalDataSource{
    override fun getAllLocations(): Flow<List<StoredLocation>> {
        return dao.getAllLocations()
    }

    override suspend fun insertLocation(storedLocation: StoredLocation): Long {
        return dao.insertLocation(storedLocation)
    }

    override suspend fun deleteLocation(storedLocation: StoredLocation): Int {
        return dao.deleteLocation(storedLocation)
    }

    companion object{

       private var INSTANCE : WeatherLocalDataSource ?=null
        fun getInstance( context: Context): WeatherLocalDataSource{
            return INSTANCE?:synchronized (this){
                val instance = WeatherLocalDataSourceImp(DataBase.getInstance(context).getLocationDao())
                INSTANCE=instance
                instance
            }
        }
    }
}