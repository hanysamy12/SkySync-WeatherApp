package com.example.skysync.repo

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewModelScope
import com.example.skysync.helper.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.locationDataStore: DataStore<Preferences> by preferencesDataStore(Constants.LOCATION_DATASTORE_NAME)
val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(Constants.SETTINGS_DATASTORE_NAME)

class DataStoreRepositoryImp(val application: Application) : DataStoreRepository {
    override suspend fun addLatLongToSharedPref(lat: Double, lon: Double) {
        application.locationDataStore.edit { pref ->
            pref[Constants.CURRENT_LAT_KEY] = lat
            pref[Constants.CURRENT_LON_KEY] = lon
        }
    }



    override fun getLatLongFromDataStore(): Flow<Pair<Double?, Double?>> {
        return application.locationDataStore.data.map { pref ->
            val lat = pref[Constants.CURRENT_LAT_KEY]
            val lon = pref[Constants.CURRENT_LON_KEY]
            Pair(lat, lon)
        }
    }

    override suspend fun setLanguage(language: String) {
        // Log.i(TAG, "setLanguage: $language")
        application.settingsDataStore.edit { settings ->
            settings[Constants.LANGUAGE_KEY] = language
        }
    }
    override suspend fun getLanguage(): Flow<String> {
        return application.settingsDataStore.data
            .map { it[Constants.LANGUAGE_KEY] ?: "en" }
            .distinctUntilChanged()
    }

    override suspend fun saveLocationWay(locationWay: String) {
        application.settingsDataStore.edit { settings ->
            settings[Constants.LOCATION_WAY_KEY] = locationWay
        }
    }

    override suspend fun saveTemperatureUnit(unit: String) {
        application.settingsDataStore.edit { settings ->
            settings[Constants.TEMPERATURE_UNIT] = unit
        }
    }

    override suspend fun getTemperatureUnit(): String {
        return application.settingsDataStore.data
            .map { it[Constants.TEMPERATURE_UNIT] ?: "metric" }
            .first()
    }

    override suspend fun saveWindUnit(unit: String) {
        application.settingsDataStore.edit { settings ->
            settings[Constants.WIND_SPEED_UNIT] = unit
        }
    }

    override suspend fun getWindUnit(): String {
        return application.settingsDataStore.data
            .map { it[Constants.WIND_SPEED_UNIT] ?: "meter" }
            .first()
    }

    override suspend fun getSettings(): Map<String, String> {
        return application.settingsDataStore.data.map { settings ->
            mapOf(
                Constants.SETTINGS_LANGUAGE to (settings[Constants.LANGUAGE_KEY] ?: "en"),
                Constants.SETTINGS_LOCATION to (settings[Constants.LOCATION_WAY_KEY] ?: "gps"),
                Constants.SETTINGS_TEMP to (settings[Constants.TEMPERATURE_UNIT] ?: "metric"),
                Constants.SETTINGS_WIND to (settings[Constants.WIND_SPEED_UNIT] ?: "meter")
            )
        }.first()
    }
}