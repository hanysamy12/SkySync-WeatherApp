package com.example.skysync.settings.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skysync.repo.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "SettingsViewModelImp"

class SettingsViewModelImp(private val dataStoreRepository: DataStoreRepository) : SettingsViewModel, ViewModel() {

    override fun setLanguage(language: String) {
        Log.i(TAG, "setLanguage: $language")
        viewModelScope.launch {
           dataStoreRepository.setLanguage(language)
        }

    }

    override fun setLocationWay(locationWay: String) {
        Log.i(TAG, "setLocationWay: $locationWay")
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveLocationWay(locationWay)
        }
    }

    override fun setTempUnit(temperatureUnit: String) {
        Log.i(TAG, "setTempMeasure: $temperatureUnit")
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveTemperatureUnit(temperatureUnit)
        }
    }

    override fun setWindUnit(windUnit: String) {
        Log.i(TAG, "setWindMeasure: $windUnit")
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveWindUnit(windUnit)
        }
    }

    override suspend fun getCredentialFromPref(): Map<String, String> {
        return dataStoreRepository.getSettings()

    }

}

class SettingsViewModelFactory(private val dataStoreRepository: DataStoreRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModelImp(dataStoreRepository) as T
    }
}