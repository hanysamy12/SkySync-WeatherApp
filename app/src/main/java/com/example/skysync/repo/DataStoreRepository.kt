package com.example.skysync.repo

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    fun getLatLongFromDataStore(): Flow<Pair<Double?, Double?>>
    suspend fun addLatLongToSharedPref(lat: Double, lon: Double)
    suspend fun setLanguage(language: String)
    suspend fun getLanguage(): Flow<String>
    suspend fun saveLocationWay(locationWay: String)
    suspend fun saveTemperatureUnit(unit: String)
    suspend fun getTemperatureUnit(): String
    suspend fun saveWindUnit(unit: String)
    suspend fun getWindUnit(): String
    suspend fun getSettings(): Map<String, String>


}