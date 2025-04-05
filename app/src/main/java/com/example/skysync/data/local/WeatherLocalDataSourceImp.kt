package com.example.skysync.data.local

import android.content.Context
import com.example.skysync.models.Alert
import com.example.skysync.models.StoredLocation
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class WeatherLocalDataSourceImp private constructor(private val dao: LocationsDAO,private val alertsDAO: AlertsDAO):
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

    override fun getAlerts(): Flow<List<Alert>> {
        return alertsDAO.getAllLocations()
    }

    override suspend fun insertAlert(alert: Alert): Long {
        return alertsDAO.insertLocation(alert)
    }

    override suspend fun deleteAlert(alertId: UUID): Int {
        return alertsDAO.deleteLocation(alertId)
    }

    companion object{

       private var INSTANCE : WeatherLocalDataSource ?=null
        fun getInstance( context: Context): WeatherLocalDataSource{
            return INSTANCE?:synchronized (this){
                val database = DataBase.getInstance(context)
                val instance = WeatherLocalDataSourceImp(database.getLocationDao(),database.getAlertDao())
                INSTANCE=instance
                instance
            }
        }
    }
}