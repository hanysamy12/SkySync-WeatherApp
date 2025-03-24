package com.example.skysync.settings.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skysync.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "SettingsViewModelImp"
val Context.settingsDataStore: DataStore<Preferences>by preferencesDataStore(Constants.SETTINGS_DATASTORE_NAME)
class SettingsViewModelImp (private val application: Application): SettingsViewModel, ViewModel() {
    override fun setLanguage(language: String) {
        Log.i(TAG, "setLanguage: $language")
        viewModelScope.launch{
            application.settingsDataStore.edit {
                settings ->settings[Constants.LANGUAGE_KEY] = language
            }
        }
    }

    override fun setLocationWay(locationWay: String) {
        Log.i(TAG, "setLocationWay: $locationWay")
        viewModelScope.launch (Dispatchers.IO){
            application.settingsDataStore.edit {
                settings ->settings[Constants.LOCATION_WAY_KEY] = locationWay
            }
        }
    }

    override fun setTempUnit(temperatureUnit: String) {
        Log.i(TAG, "setTempMeasure: $temperatureUnit")
        viewModelScope.launch (Dispatchers.IO){
            application.settingsDataStore.edit {
                settings ->settings[Constants.TEMPERATURE_UNIT] = temperatureUnit
            }
        }
    }

    override fun setWindUnit(windUnit: String) {
        Log.i(TAG, "setWindMeasure: $windUnit")
        viewModelScope.launch (Dispatchers.IO){
            application.settingsDataStore.edit {
                settings -> settings[Constants.WIND_SPEED_UNIT] = windUnit
            }
        }
    }
}

class SettingsViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModelImp(application) as T
    }
}