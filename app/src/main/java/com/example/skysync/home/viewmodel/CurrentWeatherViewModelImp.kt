package com.example.skysync.home.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skysync.Constants
import com.example.skysync.data.locationDataStore
import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import com.example.skysync.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG = "CurrentWeatherViewModel"
class CurrentWeatherViewModelImp(private val context: Context,private val repo: WeatherRepository) : CurrentWeatherViewModel,
    ViewModel() {
    private val mutableWeather: MutableLiveData<CurrentWeatherResponse> = MutableLiveData()
    val weather: LiveData<CurrentWeatherResponse> = mutableWeather

    private val mutableForecast: MutableLiveData<ForecastWeatherResponse> = MutableLiveData()
    val forecast: LiveData<ForecastWeatherResponse> = mutableForecast

    private val mutableMessage: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = mutableMessage


    override  fun getCurrentWeather(
         lang: String, unit : String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val location =getLatLongFromDataStore().first()
            try {
                val result = repo.getCurrentWeather(location.first, location.second, lang,unit)
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

        lang: String,
        unit: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val location =getLatLongFromDataStore().first()

            try {
                val result = repo.getForecast(location.first, location.second, "en","metric")
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

    private fun getLatLongFromDataStore(): Flow<Pair<Long?, Long?>>
    {
        return context.locationDataStore.data.map {
            pref->val lat =pref[Constants.CURRENT_LAT_KEY]
            val lon = pref[Constants.CURRENT_LON_KEY]
            Pair(lat,lon)
        }
    }
}
class HomeViewModelFactory(private val context: Context,private val repo: WeatherRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>    ): T {
        return CurrentWeatherViewModelImp(context,repo) as T
    }
}