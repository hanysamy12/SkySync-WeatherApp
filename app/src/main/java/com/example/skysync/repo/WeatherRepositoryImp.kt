package com.example.skysync.repo

import com.example.skysync.data.local.WeatherLocalDataSource
import com.example.skysync.data.remote.WeatherRemoteDataSource
import com.example.skysync.models.Alert
import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import com.example.skysync.models.SearchLocationsResponseItem
import com.example.skysync.models.StoredLocation
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class WeatherRepositoryImp(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource
) :
    WeatherRepository {

    override suspend fun getCurrentWeather(
        lat: Double?,
        lon: Double?,
        language: String,
        unit: String
    ): Flow<CurrentWeatherResponse> {
        return remoteDataSource.getCurrentWeather(lat, lon, language, unit)
    }

    override suspend fun getForecast(
        lat: Double?,
        lon: Double?,
        language: String,
        unit: String
    ): Flow<ForecastWeatherResponse> {
        return remoteDataSource.getForecast(lat, lon, language, unit)
    }

    override suspend fun searchLocation(searchQuery: String): Flow<List<SearchLocationsResponseItem>> {
        return remoteDataSource.searchForLocation(searchQuery)
    }

    override fun getFavoriteLocations(): Flow<List<StoredLocation>> {
        return localDataSource.getAllLocations()
    }


    override suspend fun adNewFavoriteLocations(storedLocation: StoredLocation): Long {
        return localDataSource.insertLocation(storedLocation)
    }

    override suspend fun deleteFavoriteLocation(storedLocation: StoredLocation): Int {
        return localDataSource.deleteLocation(storedLocation)
    }

    override fun getAlerts(): Flow<List<Alert>> {
        return localDataSource.getAlerts()
    }

    override suspend fun adNewAlert(alert: Alert): Long {
        return localDataSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alertId: UUID): Int {
        return localDataSource.deleteAlert(alertId)
    }
}