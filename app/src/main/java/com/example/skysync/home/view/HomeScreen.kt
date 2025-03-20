package com.example.skysync.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val TAG = "HomeScreen"

@Preview()
@Composable
fun HomeScreen(/*navController: NavController, viewModel: CurrentWeatherViewModelImp*/) {
    val horizontalScroll = rememberScrollState()
    val date = "20,mar"
    val time = "10:23 PM"
    val feelsLike = "15"
    val currentTemperature = -5
    val tempMeasure = "C"
    /*viewModel.getCurrentWeather(29.394859, 30.9028837, "en")
    val weatherState = viewModel.weather.observeAsState()
    val messageState = viewModel.message.observeAsState()
    val currentWeather = weatherState.value

    Log.i(TAG, "HomeScreen: ${currentWeather?.weather?.get(0)?.description}")*/
    Column(
        modifier = Modifier
            .verticalScroll(horizontalScroll)
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            //.height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Fayoum", fontSize = 25.sp)
                Text("$date  $time", fontSize = 20.sp)
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Clear", fontSize = 20.sp)
                Text("Feels $feelsLike", fontSize = 20.sp)
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top) {
             Text("$currentTemperature", fontSize = 120.sp)
             Text(tempMeasure, fontSize = 20.sp , modifier = Modifier.padding(top=12.dp))
        }
        /*Text(
                   text = "Home Screen, ${currentWeather?.weather?.get(0)?.description}",
                   style = MaterialTheme.typography.headlineLarge
               )*/
    }
}