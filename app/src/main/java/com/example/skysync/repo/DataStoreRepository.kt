package com.example.skysync.repo

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun addLatLongToSharedPref(lat: Double, lon: Double)
    fun getLatLongFromDataStore(): Flow<Pair<Long?, Long?>>
    suspend fun setLanguage(language: String)
    suspend fun getLanguage(): Flow<String>
    suspend fun saveLocationWay(locationWay: String)
    suspend fun saveTemperatureUnit(unit: String)
    suspend fun getTemperatureUnit(): String
    suspend fun saveWindUnit(unit: String)
    suspend fun getWindUnit(): String
    suspend fun getSettings(): Map<String, String>

}