package com.example.skysync.home.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skysync.data.Location
import com.example.skysync.helper.Response
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
    private val dataStoreRepo: DataStoreRepository,
    private val location: Location
) : CurrentWeatherViewModel, ViewModel() {

    private val mutableWeather =
        MutableStateFlow<Response<CurrentWeatherResponse>>(Response.Loading)
    val weather: StateFlow<Response<CurrentWeatherResponse>> = mutableWeather

    private val mutableForecast =
        MutableStateFlow<Response<ForecastWeatherResponse>>(Response.Loading)
    val forecast: StateFlow<Response<ForecastWeatherResponse>> = mutableForecast

    private val mutableMessage: MutableLiveData<Response<String>> = MutableLiveData()

    private var language by mutableStateOf("en")
    private var temperatureUnit by mutableStateOf("metric")
    private var windUnit by mutableStateOf("meter")

    //Fahrenheit use units=imperial
    //Celsius use units=metric
    // Kelvin use units=standard

    override fun loadInitialValues(
        lat: Double?,
        lon: Double?
    ): Triple<String, String, String> {
        Log.i(TAG, "loadInitialValues: lat $lat /// lon $lon")
        viewModelScope.launch {
            mutableWeather.value = Response.Loading
            mutableForecast.value = Response.Loading
            try {
                if (lat == null || lon == null) {
                    language = dataStoreRepo.getLanguage().first()
                    temperatureUnit = dataStoreRepo.getTemperatureUnit()
                    windUnit = dataStoreRepo.getWindUnit()
                    Log.i(TAG, "loadInitialValues: $lat ,, $lon null")
                    location.getLocation().let { (lat, lon) ->
                        getCurrentWeather(lat, lon, language, temperatureUnit)
                        getForecast(lat, lon, language, temperatureUnit)
                    }
                } else {
                    Log.i(TAG, "loadInitialValues: $lat ,, $lon  not Null")
                    getCurrentWeather(lat, lon, language, temperatureUnit)
                    getForecast(lat, lon, language, temperatureUnit)
                }
            } catch (e: Exception) {
                mutableMessage.value = Response.Failure(e)
            }
        }
        return Triple(language, temperatureUnit, windUnit)
    }

    private suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        language: String,
        unit: String
    ) {
        Log.i(TAG, "getCurrentWeather: Lang is ///$language / $unit/ $lat/ $lon////")
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

    private suspend fun getForecast(lat: Double, lon: Double, language: String, unit: String) {

        try {
            Log.i(TAG, "getCurrentForecast:  $lat/ $lon////")

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
    private val dataStoreRepo: DataStoreRepository,
    private val location: Location
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModelImp(repo, dataStoreRepo, location) as T
    }
}