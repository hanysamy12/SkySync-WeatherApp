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
import com.example.skysync.helper.NetworkObserver
import com.example.skysync.helper.NetworkStatus
import com.example.skysync.helper.Response
import com.example.skysync.helper.Response.Failure
import com.example.skysync.helper.Response.Loading
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
    private val location: Location,
    private val networkObserver: NetworkObserver
) : CurrentWeatherViewModel, ViewModel() {

    private val mutableWeather =
        MutableStateFlow<Response<CurrentWeatherResponse>>(Loading)
    val weather: StateFlow<Response<CurrentWeatherResponse>> = mutableWeather

    private val mutableForecast =
        MutableStateFlow<Response<ForecastWeatherResponse>>(Loading)
    val forecast: StateFlow<Response<ForecastWeatherResponse>> = mutableForecast

    private val mutableMessage: MutableLiveData<Response<String>> = MutableLiveData()
    private val mutableShowConnectionLost = MutableStateFlow(false)
    val showConnectionLost: StateFlow<Boolean> = mutableShowConnectionLost
    private var language by mutableStateOf("en")
    private var temperatureUnit by mutableStateOf("metric")
    private var windUnit by mutableStateOf("meter")

    //Fahrenheit use units=imperial
    //Celsius use units=metric
    // Kelvin use units=standard
    var tempLat: Double? = null
    var tempLon: Double? = null


    override fun loadInitialValues(
        lat: Double?,
        lon: Double?
    ): Triple<String, String, String> {
        tempLat = lat
        tempLon = lon
        viewModelScope.launch {
            mutableWeather.value = Loading
            mutableForecast.value = Loading
            networkObserver.networkStatus.collect { state ->
                when (state) {
                    NetworkStatus.Available -> {
                        mutableShowConnectionLost.value = false
                        try {
                            if (lat == null || lon == null) {
                                language = dataStoreRepo.getLanguage().first()
                                temperatureUnit = dataStoreRepo.getTemperatureUnit()
                                windUnit = dataStoreRepo.getWindUnit()
                                Log.i(TAG, "loadInitialValues: $lat ,, $lon null")
                                // 29.305581206555264/ 30.841450095176697////
                                location.getLocation().let { (locationLat, locationLon) ->
                                    if (lat?.toInt() == tempLat?.toInt() && lon?.toInt() == tempLon?.toInt()) {
                                        getCurrentWeather(locationLat, locationLon, language, temperatureUnit)
                                        getForecast(locationLat, locationLon, language, temperatureUnit)
                                    }
                                }
                               /* getCurrentWeather(29.305, 30.841450, language, temperatureUnit)
                                getForecast(29.305, 30.841450, language, temperatureUnit)*/
                            } else {
                                Log.i(TAG, "loadInitialValues: $lat ,, $lon  not Null")
                                getCurrentWeather(lat, lon, language, temperatureUnit)
                                getForecast(lat, lon, language, temperatureUnit)
                            }
                        } catch (e: Exception) {
                            mutableMessage.value = Failure(e)
                        }
                    }

                    NetworkStatus.Lost -> {
                        Log.i(TAG, "observeNetwork: NO Connection")
                        mutableShowConnectionLost.value = true
                    }
                }
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
                .catch { error -> mutableWeather.value = Failure(error) }
                .collect { weather ->
                    mutableWeather.value = Response.Success(weather)
                }
        } catch (e: Exception) {
            mutableMessage.value = Failure(e)

        }
    }

    private suspend fun getForecast(lat: Double, lon: Double, language: String, unit: String) {

        try {
            Log.i(TAG, "getCurrentForecast:  $lat/ $lon////")

            repo.getForecast(lat, lon, language, unit)
                .catch { error -> mutableForecast.value = Failure(error) }
                .collect { forecast ->
                    mutableForecast.value = Response.Success(forecast)
                }

        } catch (e: Exception) {
            mutableMessage.value = Failure(e)
        }
    }
}

class HomeViewModelFactory(
    private val repo: WeatherRepository,
    private val dataStoreRepo: DataStoreRepository,
    private val location: Location,
    private val networkObserver: NetworkObserver
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModelImp(repo, dataStoreRepo, location, networkObserver) as T
    }
}