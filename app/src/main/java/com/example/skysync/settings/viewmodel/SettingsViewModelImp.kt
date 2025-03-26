package com.example.skysync.settings.viewmodel

import android.app.Application
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skysync.Constants
import com.example.skysync.repo.settingsDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG = "SettingsViewModelImp"

class SettingsViewModelImp(private val application: Application) : SettingsViewModel, ViewModel() {
    override fun setLanguage(language: String) {
        Log.i(TAG, "setLanguage: $language")
        viewModelScope.launch {
            application.settingsDataStore.edit { settings ->
                settings[Constants.LANGUAGE_KEY] = language
            }
        }
    }

    override fun setLocationWay(locationWay: String) {
        Log.i(TAG, "setLocationWay: $locationWay")
        viewModelScope.launch(Dispatchers.IO) {
            application.settingsDataStore.edit { settings ->
                settings[Constants.LOCATION_WAY_KEY] = locationWay
            }
        }
    }

    override fun setTempUnit(temperatureUnit: String) {
        Log.i(TAG, "setTempMeasure: $temperatureUnit")
        viewModelScope.launch(Dispatchers.IO) {
            application.settingsDataStore.edit { settings ->
                settings[Constants.TEMPERATURE_UNIT] = temperatureUnit
            }
        }
    }

    override fun setWindUnit(windUnit: String) {
        Log.i(TAG, "setWindMeasure: $windUnit")
        viewModelScope.launch(Dispatchers.IO) {
            application.settingsDataStore.edit { settings ->
                settings[Constants.WIND_SPEED_UNIT] = windUnit
            }
        }
    }

    override suspend fun getCredentialFromPref(): Map<String, String> {
        return application.settingsDataStore.data
            .map { settings ->
                mapOf(
                    Constants.SETTINGS_LANGUAGE to (settings[Constants.LANGUAGE_KEY] ?: "en"),
                    Constants.SETTINGS_LOCATION to (
                            settings[Constants.LOCATION_WAY_KEY] ?: "gps"
                            ),
                    Constants.SETTINGS_TEMP to (
                            settings[Constants.TEMPERATURE_UNIT] ?: "metric"
                            ),
                    Constants.SETTINGS_WIND to (
                            settings[Constants.WIND_SPEED_UNIT] ?: "meter"
                            )
                )
            }
            .first()
    }
}

class SettingsViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModelImp(application) as T
    }
}