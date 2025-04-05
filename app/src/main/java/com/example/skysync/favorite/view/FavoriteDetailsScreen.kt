package com.example.skysync.favorite.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.skysync.R
import com.example.skysync.helper.Response
import com.example.skysync.home.view.CurrentWeatherShow
import com.example.skysync.home.view.ForecastShow
import com.example.skysync.home.view.MessageShow
import com.example.skysync.home.view.ProgressShow
import com.example.skysync.home.viewmodel.CurrentWeatherViewModelImp

//private const val TAG = "HomeScreen"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FavoriteDetailsScreen(viewModel: CurrentWeatherViewModelImp, lat: Double, lon: Double) {
    val fahrenheitSymbol = stringResource(R.string.fahrenheit_symbol)
    val celsiusSymbol = stringResource(R.string.celsius_symbol)
    val kelvinSymbol = stringResource(R.string.kelvin_symbol)
    val mileSymbol = stringResource(R.string.miles_hour)
    val meterSymbol = stringResource(R.string.meter_sec)
    var lang = rememberSaveable { mutableStateOf("") }
    var tempUnitSymbol = rememberSaveable { mutableStateOf("") }
    var windUnit = rememberSaveable { mutableStateOf("") }
    LaunchedEffect(lat, lon) {
        val (language, temperatureUnit, windSpeedUnit) = viewModel.loadInitialValues(lat, lon)
        lang.value = language
        windUnit.value = when (windSpeedUnit) {
            "mile" -> mileSymbol
            "meter" -> meterSymbol
            else -> meterSymbol
        }
        tempUnitSymbol.value = when (temperatureUnit) {
            "imperial" -> fahrenheitSymbol
            "metric" -> celsiusSymbol
            "standard" -> kelvinSymbol
            else -> celsiusSymbol
        }
    }
    val uiWeatherState by viewModel.weather.collectAsStateWithLifecycle()
    val uiForecastState by viewModel.forecast.collectAsStateWithLifecycle()

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_home),
            contentDescription = "background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            when (uiWeatherState) {
                is Response.Success -> {
                    ///////
                    when (uiForecastState) {
                        is Response.Success -> {
                            val forecast = (uiForecastState as Response.Success).data
                            val currentWeather = (uiWeatherState as Response.Success).data
                            CurrentWeatherShow(
                                currentWeather,
                                lang.value,
                                tempUnitSymbol.value,
                                windUnit.value
                            )
                            ForecastShow(forecast, lang.value, tempUnitSymbol.value)
                        }

                        is Response.Failure -> {
                            val msg = (uiForecastState as Response.Failure).toString()
                            MessageShow(msg)
                        }

                        is Response.Loading -> {
                            //  ProgressShow()
                        }
                    }

                }

                is Response.Failure -> {
                    val msg = (uiWeatherState as Response.Failure).toString()
                    MessageShow(msg)
                }

                is Response.Loading -> {
                    ProgressShow()
                }
            }

        }
    }
}









