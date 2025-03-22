package com.example.skysync.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import com.example.skysync.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "CurrentWeatherViewModel"
class CurrentWeatherViewModelImp(private val repo: WeatherRepository) : CurrentWeatherViewModel,
    ViewModel() {
    private val mutableWeather: MutableLiveData<CurrentWeatherResponse> = MutableLiveData()
    val weather: LiveData<CurrentWeatherResponse> = mutableWeather

    private val mutableForecast: MutableLiveData<ForecastWeatherResponse> = MutableLiveData()
    val forecast: LiveData<ForecastWeatherResponse> = mutableForecast

    private val mutableMessage: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = mutableMessage
    override  fun getCurrentWeather(
        lat: Double, lon: Double, lang: String, unit : String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = null//repo.getCurrentWeather(lat, lon, lang,unit)
                if (result != null) {
                    val weather: CurrentWeatherResponse = result
                    mutableWeather.postValue(weather)
                    Log.i(TAG, "getCurrentWeather: $weather")
                } else {
                    mutableMessage.postValue("Tray Again Later")
                }
            } catch (e: Exception) {
                mutableMessage.postValue("Error ${e.message}")
            }
        }
    }

    override fun getForecast(
        lat: Double,
        lon: Double,
        lang: String,
        unit: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = null //repo.getForecast(lat, lon, lang,unit)
                if (result != null) {
                    val forecast: ForecastWeatherResponse = result
                    mutableForecast.postValue(forecast)
                    Log.i(TAG, "getCurrentWeather: $weather")
                } else {
                    mutableMessage.postValue("Tray Again Later")
                }
            } catch (e: Exception) {
                mutableMessage.postValue("Error ${e.message}")
            }
        }
    }
}
class HomeViewModelFactory(private val repo: WeatherRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>    ): T {
        return CurrentWeatherViewModelImp(repo) as T
    }
}