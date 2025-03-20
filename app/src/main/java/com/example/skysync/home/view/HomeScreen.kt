package com.example.skysync.home.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.skysync.home.viewmodel.CurrentWeatherViewModelImp

private const val TAG = "HomeScreen"
@Composable
fun HomeScreen(navController: NavController, viewModel: CurrentWeatherViewModelImp) {

    viewModel.getCurrentWeather(29.394859, 30.9028837, "en")
    val weatherState = viewModel.weather.observeAsState()
    val messageState = viewModel.message.observeAsState()
val currentWeather = weatherState.value

    Log.i(TAG, "HomeScreen: ${currentWeather?.weather?.get(0)?.description}")
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Home Screen, ${currentWeather?.weather?.get(0)?.description}",
            style = MaterialTheme.typography.headlineLarge
        )
    }
}