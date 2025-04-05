package com.example.skysync.home.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.skysync.R
import com.example.skysync.helper.Response
import com.example.skysync.home.viewmodel.CurrentWeatherViewModelImp
import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import com.example.skysync.models.ListItem
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewModel: CurrentWeatherViewModelImp, lat: Double?, lon: Double?) {
    val fahrenheitSymbol = stringResource(R.string.fahrenheit_symbol)
    val celsiusSymbol = stringResource(R.string.celsius_symbol)
    val kelvinSymbol = stringResource(R.string.kelvin_symbol)
    val mileSymbol = stringResource(R.string.miles_hour)
    val meterSymbol = stringResource(R.string.meter_sec)

    var lang = rememberSaveable { mutableStateOf("") }
    var tempUnitSymbol = rememberSaveable { mutableStateOf("") }
    var windUnit = rememberSaveable { mutableStateOf("") }

    var showMessage by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
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
    val uiConnectionState by viewModel.showConnectionLost.collectAsStateWithLifecycle()
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = rememberAsyncImagePainter(
                R.drawable.bg_simple_stars,
                imageLoader = ImageLoader(LocalContext.current).newBuilder()
                    .components {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            add(ImageDecoderDecoder.Factory())
                        }
                    }
                    .build()),
            contentDescription = "background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                when (uiWeatherState) {
                    is Response.Success -> {
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

                            is Response.Loading -> {}
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
            if (showMessage) {
                MessageShow("Connection Lost")
            }
            showMessage = when (uiConnectionState) {
                true -> true
                false -> false
            }

        }

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CurrentWeatherShow(
    currentWeather: CurrentWeatherResponse?,
    lang: String,
    tempUnit: String,
    windUnit: String
) {
    val currentDateTimePair = convertDateTime(currentWeather?.dt?.toLong() ?: 0, lang)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    Icon(
                        painterResource(R.drawable.ic_location_bin),
                        contentDescription = "sunrise icon",
                        // modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(2f),
                        horizontalAlignment = Alignment.Start
                    ) {


                        Text("${currentWeather?.name}", fontSize = 20.sp)
                        Text("${currentDateTimePair.first}  ", fontSize = 16.sp)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        val temp = if (lang == "ar") {
                            numEnToAr(
                                (currentWeather?.main?.feelsLike
                                    ?: currentWeather?.main?.temp).toString().toDouble()
                                    .roundToInt().toString()
                            )
                        } else {
                            currentWeather?.main?.feelsLike ?: currentWeather?.main?.temp.toString()
                                .toDouble().roundToInt().toString()
                        }
                        Text("${currentWeather?.weather?.get(0)?.description}", fontSize = 16.sp)
                        Text(
                            "${stringResource(R.string.feels)} $temp",
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {
                    val temp = if (lang == "ar") {
                        numEnToAr(
                            currentWeather?.main?.temp.toString().toDouble().roundToInt().toString()
                        )
                    } else {
                        currentWeather?.main?.temp.toString().toDouble().roundToInt().toString()
                    }
                    Text(temp, fontSize = 100.sp)
                    Text(tempUnit, fontSize = 16.sp, modifier = Modifier.padding(top = 12.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        "${stringResource(R.string.now)} ${currentDateTimePair.second}",
                        fontSize = 12.sp
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        painterResource(R.drawable.ic_sunrise),
                        contentDescription = "sunrise icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified
                    )
                    val sunRiseTime =
                        convertDateTime(currentWeather?.sys?.sunrise?.toLong() ?: 0, lang)

                    Text(sunRiseTime.second, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painterResource(R.drawable.ic_sunrise),
                        contentDescription = "sunset icon",
                        modifier = Modifier.size(20.dp)
                    )
                    val sunSetTime =
                        convertDateTime(currentWeather?.sys?.sunset?.toLong() ?: 0, lang)

                    Text(sunSetTime.second, fontSize = 14.sp)
                }
            }




            Row( // parent
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = .3f),
                        RoundedCornerShape(16.dp)
                    )
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
                                .weight(1f),
                            tint = Color.Unspecified
                        )

                        Column(modifier = Modifier.weight(2f)) {
                            Text(stringResource(R.string.humidity), fontSize = 14.sp)
                            val humidity = if (lang == "ar") {
                                numEnToAr(currentWeather?.main?.humidity.toString())
                            } else {
                                currentWeather?.main?.humidity.toString()
                            }
                            Text(
                                "$humidity ${stringResource(R.string.percentage_sign)}",
                                fontSize = 12.sp
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .height(2.dp)
                            .background(colorResource(id = R.color.white).copy(alpha = .3f))
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
                                .weight(1f), tint = Color.Unspecified
                        )
                        Column(modifier = Modifier.weight(2f)) {
                            Text(stringResource(R.string.wind_speed), fontSize = 14.sp)
                            val speed = if (lang == "ar") {
                                numEnToAr(currentWeather?.wind?.speed.toString())
                            } else {
                                currentWeather?.wind?.speed.toString()
                            }
                            Text("$speed $windUnit", fontSize = 12.sp)
                        }
                    }
                }

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
                                .weight(1f), tint = Color.Unspecified
                        )
                        Column(modifier = Modifier.weight(2f)) {
                            Text(stringResource(R.string.pressure), fontSize = 14.sp)
                            val pressure = if (lang == "ar") {
                                numEnToAr(currentWeather?.main?.pressure.toString())
                            } else {
                                currentWeather?.main?.pressure.toString()
                            }
                            Text(pressure, fontSize = 12.sp)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .height(2.dp)
                            .background(colorResource(id = R.color.white).copy(alpha = .3f))
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
                                .weight(1f), tint = Color.Unspecified
                        )
                        Column(modifier = Modifier.weight(2f)) {
                            Text(stringResource(R.string.cloud), fontSize = 14.sp)
                            val cloud = if (lang == "ar") {
                                numEnToAr(currentWeather?.clouds?.all.toString())
                            } else {
                                currentWeather?.clouds?.all.toString()
                            }
                            Text(
                                "$cloud ${stringResource(R.string.percentage_sign)}",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastShow(forecast: ForecastWeatherResponse?, lang: String, temUnit: String) {

    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(
            modifier = Modifier.background(
                MaterialTheme.colorScheme.primary.copy(alpha = .3f),
                shape = RoundedCornerShape(16.dp)
            )
        ) {
            items(8) { i -> HourItem(forecast?.list?.get(i), lang, temUnit) }

        }
        val daysList =
            forecast?.list?.filterIndexed { index, _ -> index == 0 || index % 8 == 0 }
        daysList?.forEach { day -> DaysItem(day, lang, temUnit) }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourItem(hourItem: ListItem?, lang: String, tempUnit: String) {

    Column(
        modifier = Modifier
            .height(120.dp)
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val hour = convertDateTime(hourItem?.dt?.toLong() ?: 0, lang)
        Text(hour.second, fontSize = 14.sp)
        GlideImage(
            model = "https://openweathermap.org/img/wn/${hourItem?.weather?.firstOrNull()?.icon ?: "01d"}@2x.png",
            contentDescription = "Product Image",
            modifier = Modifier
                .size(40.dp), colorFilter = null
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            val temp = if (lang == "ar") {
                numEnToAr(hourItem?.main?.temp.toString().toDouble().roundToInt().toString())
            } else {
                hourItem?.main?.temp.toString().toDouble().toDouble().roundToInt().toString()
            }
            Text(temp, fontSize = 14.sp)
            Text(tempUnit, fontSize = 10.sp)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaysItem(dayItem: ListItem?, lang: String, temUnit: String) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(vertical = 8.dp)
            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(24.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            val day = convertDateTime(dayItem?.dt?.toLong() ?: 0, lang)
            Text(day.first, fontSize = 16.sp)
        }

        GlideImage(
            model = "https://openweathermap.org/img/wn/${dayItem?.weather?.firstOrNull()?.icon ?: "01d"}@2x.png",
            contentDescription = "Product Image",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape), colorFilter = null
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp), horizontalArrangement = Arrangement.End
        ) {
            Row(//max
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                val temp = if (lang == "ar") {
                    numEnToAr(dayItem?.main?.temp.toString().toDouble().toInt().toString())
                } else {
                    dayItem?.main?.temp.toString().toDouble().toInt().toString()
                }
                Text(temp, fontSize = 14.sp)
                Text(temUnit, fontSize = 8.sp)
            }
            Text("/", fontSize = 20.sp)
            Row(//min

                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                val temp = if (lang == "ar") {
                    numEnToAr(dayItem?.main?.temp.toString().toDouble().roundToInt().toString())
                } else {
                    dayItem?.main?.temp.toString().toDouble().roundToInt().toString()
                }
                Text(temp, fontSize = 14.sp)
                Text(temUnit, fontSize = 8.sp)
            }
        }
    }
}

@Composable
fun ProgressShow() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(300.dp))
        LinearProgressIndicator()
        Text(stringResource(R.string.Waiting_message), fontSize = 22.sp)

    }

}

@Composable
fun MessageShow(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.Red.copy(.2f)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(message, color = Color.White, fontSize = 20.sp)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertDateTime(dateLong: Long, lang: String): Pair<String, String> {
    // Log.i("dateTime", "Home: $dateLong")
    val instance = Instant.ofEpochSecond(dateLong)
    val dateFormater =
        DateTimeFormatter.ofPattern("EEE, dd MMM", Locale(lang))
            .withZone(ZoneId.systemDefault())
    var formatedDate = dateFormater.format(instance)
    val timeFormatter =
        DateTimeFormatter.ofPattern("hh:mm a", Locale(lang))
            .withZone(ZoneId.systemDefault())
    var formatedTime = timeFormatter.format(instance)
    // Log.i("dateTime", "Home : $formatedDate /// $formatedTime")
    if (Locale(lang).language == "ar") {
        formatedTime = numEnToAr(formatedTime)
        formatedDate = numEnToAr(formatedDate)

    }
    return Pair(formatedDate, formatedTime)
}

fun numEnToAr(input: String): String {
    val englishNumbers = Regex("[0-9]")
    val arabicNumber = arrayOf("٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩")

    return englishNumbers.replace(input) {
        arabicNumber[it.value.toInt()]
    }
}

