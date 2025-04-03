package com.example.skysync.repo

import com.example.skysync.data.local.WeatherLocalDataSource
import com.example.skysync.data.remote.WeatherRemoteDataSource
import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import com.example.skysync.models.SearchLocationsResponseItem
import com.example.skysync.models.StoredLocation
import kotlinx.coroutines.flow.Flow

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
}