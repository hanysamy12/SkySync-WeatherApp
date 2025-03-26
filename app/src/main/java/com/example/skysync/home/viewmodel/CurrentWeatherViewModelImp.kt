package com.example.skysync.home.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skysync.Response
import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import com.example.skysync.repo.DataStoreRepository
import com.example.skysync.repo.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val TAG = "CurrentWeatherViewModel"

class CurrentWeatherViewModelImp(
    private val repo: WeatherRepository,
    private val dataStoreRepo: DataStoreRepository
) : CurrentWeatherViewModel,
    ViewModel() {

    private val mutableWeather =
        MutableStateFlow<Response<CurrentWeatherResponse>>(Response.Loading)
    val weather: StateFlow<Response<CurrentWeatherResponse>> = mutableWeather

    private val mutableForecast =
        MutableStateFlow<Response<ForecastWeatherResponse>>(Response.Loading)
    val forecast: StateFlow<Response<ForecastWeatherResponse>> = mutableForecast

    private val mutableMessage: MutableLiveData<Response<String>> = MutableLiveData()
    //val message: LiveData<Response<String>> = mutableMessage

    private var language by mutableStateOf("en")
    private var temperatureUnit by mutableStateOf("metric")

    /*
        override fun getHomeData() {
            viewModelScope.launch (Dispatchers.IO){
                val currentSettings = application.settingsDataStore.data.first()
                language = currentSettings[Constants.LANGUAGE_KEY] ?: "en"
                temperatureUnit = currentSettings[Constants.TEMPERATURE_UNIT] ?: "metric"
            observeSettingsChange()
            getCurrentWeather(language,temperatureUnit)
            getForecast(language,temperatureUnit)
        }}
         private fun observeSettingsChange() {
             viewModelScope.launch(Dispatchers.IO) {
                 application.settingsDataStore.data
                     .map { settings ->
                         val language = settings[Constants.LANGUAGE_KEY] ?: "en"
                         val temperatureUnit = settings[Constants.TEMPERATURE_UNIT] ?: "metric"
                         Pair(language, temperatureUnit)
                     }
                     .distinctUntilChanged()
                     .collect { (storeLanguage, storedTemperatureUnit) ->
                         language = storeLanguage
                         temperatureUnit = storedTemperatureUnit
                     }
             }
         }*/
    //Fahrenheit use units=imperial
    //Celsius use units=metric
    // Kelvin use units=standard

    override fun loadInitialValues() {
        viewModelScope.launch {
            try {
                language = dataStoreRepo.getLanguage()
                temperatureUnit = dataStoreRepo.getTemperatureUnit()
                dataStoreRepo.getLatLongFromDataStore().first().let { (lat, lon) ->
                    if (lat != null && lon != null) {
                        getCurrentWeather(lat, lon, language, temperatureUnit)
                        getForecast(lat, lon, language, temperatureUnit)
                    } else {
                        mutableMessage.value = Response.Failure(Exception("Location Not Enabled"))
                    }
                }

                language = dataStoreRepo.getLanguage()
                temperatureUnit = dataStoreRepo.getTemperatureUnit()
            } catch (e: Exception) {
                mutableMessage.value = Response.Failure(e)
            }
        }
    }


    private suspend fun getCurrentWeather(lat: Long, lon: Long, language: String, unit: String) {
        Log.i(TAG, "getCurrentWeather: Lang is ///$language / $unit//////")

        try {
            repo.getCurrentWeather(lat, lon, language, unit)
                .catch { error -> mutableWeather.value = Response.Failure(error) }
                .collect { weather ->
                    mutableWeather.value = Response.Success(weather)
                }
        } catch (e: Exception) {
            mutableMessage.value = Response.Failure(e)

        }
    }

    private suspend fun getForecast(lat: Long, lon: Long, language: String, unit: String) {

        try {
            repo.getForecast(lat, lon, language, unit)
                .catch { error -> mutableForecast.value = Response.Failure(error) }
                .collect { forecast ->
                    mutableForecast.value = Response.Success(forecast)
                }

        } catch (e: Exception) {
            mutableMessage.value = Response.Failure(e)
        }
    }
}


class HomeViewModelFactory(
    private val repo: WeatherRepository,
    private val dataStoreRepo: DataStoreRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModelImp(repo, dataStoreRepo) as T
    }
}