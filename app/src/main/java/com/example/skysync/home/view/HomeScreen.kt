package com.example.skysync.home.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skysync.R
import com.example.skysync.home.viewmodel.CurrentWeatherViewModelImp
import com.example.skysync.models.ForecastWeatherResponse
import com.example.skysync.models.ListItem
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val TAG = "HomeScreen"

//@Preview()
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen( viewModel: CurrentWeatherViewModelImp) {
    val horizontalScroll = rememberScrollState()
    viewModel.getCurrentWeather(29.394859, 30.9028837, "en","metric") //29.394859, 30.9028837
    viewModel.getForecast(29.394859, 30.9028837, "en","metric")
    val weatherState = viewModel.weather.observeAsState()
    val forecastState = viewModel.forecast.observeAsState()
    val messageState = viewModel.message.observeAsState()

    val currentWeather = weatherState.value
    val forecast = forecastState.value
    Log.i(TAG, "HomeScreen Weather : $currentWeather")
    Log.i(TAG, "HomeScreen Forecast :$forecast")
    val currentdateTimePair = convertDateTime(currentWeather?.dt?.toLong() ?: 0)
    val tempMeasure = "C"
    //val daysList = listOf(1,2,3,4,5)
    val gradientColors = listOf(
        // Color(0xFF6A11CB),
        Color(0xFF2575FC),
        Color(0xFF00B4DB)
    )


    val gradientBrush = Brush.linearGradient(
        colors = gradientColors,
        start = androidx.compose.ui.geometry.Offset(0f, 0f), // Top-left corner
        end = androidx.compose.ui.geometry.Offset.Infinite // Bottom-right corner
    )
    //Fahrenheit use units=imperial
    //Celsius use units=metric
    // Kelvin use units=standard
        
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(horizontalScroll)
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Text("$currentWeather")
            Row(
                modifier = Modifier.fillMaxWidth(),
                //.height(60.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${currentWeather?.name}", fontSize = 25.sp)
                    
                    Text("${currentdateTimePair.first}  ", fontSize = 20.sp)
                }
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${currentWeather?.weather?.get(0)?.description}", fontSize = 20.sp)
                    Text("Feels Like: ${currentWeather?.main?.feelsLike ?:currentWeather?.main?.temp}", fontSize = 16.sp)
                }
            }
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {
                    Text("${currentWeather?.main?.temp}", fontSize = 120.sp)
                    Text(tempMeasure, fontSize = 20.sp, modifier = Modifier.padding(top = 12.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    
                    Text("Now: ${currentdateTimePair.second}", fontSize = 16.sp)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {

                    Icon(
                        painterResource(R.drawable.ic_sunrise),
                        contentDescription = "sunrise icon",
                        modifier = Modifier.size(20.dp)
                    )
                    val sunRiseTime = convertDateTime(currentWeather?.sys?.sunrise?.toLong()?:0)
                    Text(sunRiseTime.second, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painterResource(R.drawable.ic_sunrise),
                        contentDescription = "sunset icon",
                        modifier = Modifier.size(20.dp)
                    )
                    val sunSetTime = convertDateTime(currentWeather?.sys?.sunset?.toLong()?:0)

                    Text(sunSetTime.second, fontSize = 18.sp)
                }
            }

            LazyRow(
                modifier = Modifier.background(
                    gradientBrush,
                    shape = RoundedCornerShape(16.dp)
                )
            ) {
                items(8) {i->HourItem(forecast?.list?.get(i))}
                    //Spacer(modifier = Modifier.width(6.dp))
                }

            Row( // parent
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gradientBrush, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier //left
                        .weight(1f)
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .fillMaxWidth(1f)
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_humidity),
                            contentDescription = "humidity icon",
                            modifier = Modifier
                                .size(22.dp)
                                .weight(1f)
                        )
                        Column(modifier = Modifier.weight(2f)) {
                            Text("Humidity", fontSize = 14.sp)
                            Text("${currentWeather?.main?.humidity} %", fontSize = 12.sp)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .height(2.dp)
                            .background(colorResource(id = R.color.white))
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_wind),
                            contentDescription = "wind icon",
                            modifier = Modifier
                                .size(22.dp)
                                .weight(1f)
                        )
                        Column(modifier = Modifier.weight(2f)) {
                            Text("Wind Speed", fontSize = 14.sp)
                            Text("${currentWeather?.wind?.speed}", fontSize = 12.sp)
                        }
                    }
                }
                /*Box(
                    modifier = Modifier
                        .fillMaxHeight()
                      //  .padding(5.dp)
                        .width(2.dp)
                        .background(colorResource(R.color.black))
                )*/
                Column(
                    modifier = Modifier //right
                        .weight(1f)
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_pressure),
                            contentDescription = "pressure icon",
                            modifier = Modifier
                                .size(22.dp)
                                .weight(1f)
                        )
                        Column(modifier = Modifier.weight(2f)) {
                            Text("Pressure", fontSize = 14.sp)
                            Text("${currentWeather?.main?.pressure}", fontSize = 12.sp)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .height(2.dp)
                            .background(colorResource(id = R.color.white))
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_cloud),
                            contentDescription = "cloud icon",
                            modifier = Modifier
                                .size(22.dp)
                                .weight(1f)
                        )
                        Column(modifier = Modifier.weight(2f)) {
                            Text("Cloud", fontSize = 14.sp)
                            Text("${currentWeather?.clouds?.all} %", fontSize = 12.sp)
                        }
                    }
                }
            }

            val daysList = forecast?.list?.filterIndexed { index,_ ->index==0 ||index%8==0}
            daysList?.forEach {day-> DaysItem(day) }
            }
        }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HourItem(hourItem: ListItem?) {
    Log.i(TAG, "HourItem: $hourItem")
   // var hour = "forecastList?.list"
    var icon: Painter = painterResource(R.drawable.ic_settings)
    var temperature = "-13"
    var tempMeasure = "c"
    Column(
        modifier = Modifier
            //.width(60.dp)
            //  .fillMaxHeight(0.10f),
            .height(120.dp)
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val hour = convertDateTime(hourItem?.dt?.toLong()?:0)
        Text(hour.second, fontSize = 20.sp)
        Icon(icon, contentDescription = "icon description", modifier = Modifier.size(20.dp))
        Row(
            // modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            Text("${hourItem?.main?.temp}", fontSize = 20.sp)
            Text(tempMeasure, fontSize = 10.sp /*modifier = Modifier.padding(top = 12.dp)*/)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DaysItem(dayItem: ListItem?) {

    val tempMeasure = "C"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            //.height(70.dp)
            .padding(vertical = 8.dp)
            .border(2.dp, colorResource(id = R.color.black), RoundedCornerShape(24.dp)),
           // .background(colorResource(id = R.color.teal_200), RoundedCornerShape(26.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            modifier = Modifier
                //.weight(2f)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painterResource(R.drawable.ic_sunrise),
                contentDescription = "icon desc",
                modifier = Modifier.size(26.dp)
            )
            val day = convertDateTime(dayItem?.dt?.toLong() ?:0)
            Text(day.first, fontSize = 22.sp)
           // Text("${dayItem?.weather?.get(0)?.main}", fontSize = 20.sp)
        }
        Row(modifier = Modifier
            //.weight(1f)
            .padding(horizontal = 12.dp), horizontalArrangement = Arrangement.End) {
            Row(//max
                // modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                Text("${dayItem?.main?.temp}", fontSize = 20.sp)
                Text(tempMeasure, fontSize = 10.sp /*modifier = Modifier.padding(top = 12.dp)*/)
            }
            Text("/", fontSize = 20.sp)
            Row(//min
                // modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                Text("${dayItem?.main?.temp}", fontSize = 20.sp)
                Text(tempMeasure, fontSize = 10.sp /*modifier = Modifier.padding(top = 12.dp)*/)
            }
        }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun convertDateTime(dateLong : Long): Pair<String, String>{
    Log.i(TAG, "longDateTime: $dateLong")
    val instance = Instant.ofEpochSecond(dateLong)
    val dateFormater = DateTimeFormatter.ofPattern("EEE, dd MMM",Locale.ENGLISH).withZone(ZoneId.systemDefault())
    val formatedDate = dateFormater.format(instance)
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a",Locale.ENGLISH).withZone(ZoneId.systemDefault())
    val formatedTime = timeFormatter.format(instance)
    Log.i(TAG, "converted DateTime: $formatedDate --- $formatedTime")
    return Pair(formatedDate,formatedTime)
}

