package com.example.skysync.repo

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.skysync.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.locationDataStore: DataStore<Preferences> by preferencesDataStore(Constants.LOCATION_DATASTORE_NAME)
val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(Constants.SETTINGS_DATASTORE_NAME)

class DataStoreRepositoryImp(val application: Application) : DataStoreRepository {
    override suspend fun addLatLongToSharedPref(lat: Double, lon: Double) {
        application.locationDataStore.edit { pref ->
            pref[Constants.CURRENT_LAT_KEY] = lat.toLong()
            pref[Constants.CURRENT_LON_KEY] = lon.toLong()
        }
    }

    override fun getLatLongFromDataStore(): Flow<Pair<Long?, Long?>> {
        return application.locationDataStore.data.map { pref ->
            val lat = pref[Constants.CURRENT_LAT_KEY]
            val lon = pref[Constants.CURRENT_LON_KEY]
            Pair(lat, lon)
        }
    }

    override suspend fun getLanguage(): Flow<String> {
        return application.settingsDataStore.data
            .map { it[Constants.LANGUAGE_KEY] ?: "en" }
    }

    override suspend fun getTemperatureUnit(): String {
        return application.settingsDataStore.data
            .map { it[Constants.TEMPERATURE_UNIT] ?: "metric" }
            .first()
    }

    override suspend fun getWindUnit(): String {
        return application.settingsDataStore.data
            .map { it[Constants.WIND_SPEED_UNIT] ?: "meter" }
            .first()
    }

    override fun observeSettings(): Flow<Pair<String, String>> {
        return application.settingsDataStore.data
            .map { settings ->
                Pair(
                    settings[Constants.LANGUAGE_KEY] ?: "en",
                    settings[Constants.TEMPERATURE_UNIT] ?: "metric"
                )
            }
            .distinctUntilChanged()
    }
}