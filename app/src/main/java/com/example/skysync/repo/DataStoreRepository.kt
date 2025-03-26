package com.example.skysync.repo

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun addLatLongToSharedPref(lat: Double, lon: Double)
    fun getLatLongFromDataStore(): Flow<Pair<Long?, Long?>>
    suspend fun getLanguage(): String
    suspend fun getTemperatureUnit(): String
    fun observeSettings(): Flow<Pair<String, String>>
}