package com.example.skysync.home.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skysync.Constants
import com.example.skysync.Response
import com.example.skysync.data.locationDataStore
import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import com.example.skysync.repo.WeatherRepository
import com.example.skysync.settings.viewmodel.settingsDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG = "CurrentWeatherViewModel"

class CurrentWeatherViewModelImp(
    private val application: Application,
    private val repo: WeatherRepository
) : CurrentWeatherViewModel,
    ViewModel() {

    private val mutableWeather =
        MutableStateFlow<Response<CurrentWeatherResponse>>(Response.Loading)
    val weather: StateFlow<Response<CurrentWeatherResponse>> = mutableWeather

    private val mutableForecast =
        MutableStateFlow<Response<ForecastWeatherResponse>>(Response.Loading)
    val forecast: StateFlow<Response<ForecastWeatherResponse>> = mutableForecast

    private val mutableMessage: MutableLiveData<Response<String>> = MutableLiveData()

private lateinit var language: String
private lateinit var temperatureUnit : String
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
     }
    //Fahrenheit use units=imperial
    //Celsius use units=metric
    // Kelvin use units=standard


    private fun getCurrentWeather(language: String, unit: String ) {
        Log.i(TAG, "getCurrentWeather: Lang is ///$language / $unit//////")
        viewModelScope.launch(Dispatchers.IO) {
            val location = getLatLongFromDataStore().first()
            try {

                val result = repo.getCurrentWeather(location.first, location.second, language, unit)
                result
                    .catch { error -> mutableWeather.value = Response.Failure(error) }
                    .collect { weather ->
                        mutableWeather.value = Response.Success(weather)
                    }
            } catch (e: Exception) {
                mutableMessage.value = Response.Failure(e)
            }
        }
    }

    private fun getForecast(language: String, unit: String ) {


        viewModelScope.launch() {
            val location = getLatLongFromDataStore().first()

            try {
                val result = repo.getForecast(location.first, location.second, language, unit)
                result
                    .catch { error -> mutableForecast.value = Response.Failure(error) }
                    .collect { forecast ->
                        mutableForecast.value = Response.Success(forecast)
                    }

            } catch (e: Exception) {
                mutableMessage.value = Response.Failure(e)
            }
        }
    }

    private fun getLatLongFromDataStore(): Flow<Pair<Long?, Long?>> {
        return application.locationDataStore.data.map { pref ->
            val lat = pref[Constants.CURRENT_LAT_KEY]
            val lon = pref[Constants.CURRENT_LON_KEY]
            Pair(lat, lon)
        }
    }


}

class HomeViewModelFactory(private val context: Application, private val repo: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModelImp(context, repo) as T
    }
}