package com.example.skysync.settings.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skysync.helper.Constants
import com.example.skysync.R
import com.example.skysync.settings.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {

    //language
    var englishChecked by remember { mutableStateOf(false) }
    var arabicChecked by remember { mutableStateOf(false) }
    //location
    var gpsChecked by remember { mutableStateOf(false) }
    var mapChecked by remember { mutableStateOf(false) }
    //speed
    var meterChecked by remember { mutableStateOf(false) }
    var mileChecked by remember { mutableStateOf(false) }
    //temp
    var kelvinChecked by remember { mutableStateOf(false) }
    var celsiusChecked by remember { mutableStateOf(false) }
    var fahrenheitChecked by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val settings=viewModel.getCredentialFromPref()
        when(settings[Constants.SETTINGS_LANGUAGE]) {
            "en" -> englishChecked = true
            "ar" -> arabicChecked = true
        }

        // location
        when(settings[Constants.SETTINGS_LOCATION]) {
            "gps" -> gpsChecked = true
            "map" -> mapChecked = true
        }

        //  wind
        when(settings[Constants.SETTINGS_WIND]) {
            "meter" -> meterChecked = true
            "mile" -> mileChecked = true
        }

        //  temperature
        when(settings[Constants.SETTINGS_TEMP]) {
            "standard" -> kelvinChecked = true
            "metric" -> celsiusChecked = true
            "imperial" -> fahrenheitChecked = true
        }

    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ElevatedCard( //language
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp), colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_setting_languagesvg),
                        contentDescription = stringResource(R.string.language_icon),
                        modifier = Modifier.size(40.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(stringResource(R.string.language), fontSize = 18.sp)
                }

                Row(modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically) {

                    Row(
                        Modifier
                            .weight(.5f)
                            .height(30.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = englishChecked, onCheckedChange = {
                                englishChecked = it
                                arabicChecked = !it
                                    viewModel.setLanguage("en")

                            })
                        Text(stringResource(R.string.english), fontSize = 14.sp) ///Fixed
                    }
                    Row(
                        Modifier.weight(.5f),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = arabicChecked, onCheckedChange = {
                                arabicChecked = it
                                englishChecked = !it
                                viewModel.setLanguage("ar")
                            })
                        Text(stringResource(R.string.arabic), fontSize = 14.sp) ///Fixed
                    }
                }
            }
            ElevatedCard( //location
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp), colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings_map),
                        contentDescription = stringResource(R.string.location_icon),
                        modifier = Modifier.size(40.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(stringResource(R.string.location), fontSize = 18.sp)
                }

                Row(modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically) {

                    Row(
                        Modifier
                            .weight(.5f)
                            .height(30.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = gpsChecked, onCheckedChange = {
                                gpsChecked = it
                                mapChecked = !it
                                viewModel.setLocationWay("gps")
                            })
                        Text(stringResource(R.string.gps), fontSize = 14.sp)
                    }
                    Row(
                        Modifier.weight(.5f),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = mapChecked, onCheckedChange = {
                                mapChecked = it
                                gpsChecked = !it
                                viewModel.setLocationWay("map")
                            })
                        Text(stringResource(R.string.map), fontSize = 14.sp)
                    }
                }
            }
            ElevatedCard( //temp
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp), colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_setting_temp),
                        contentDescription = stringResource(R.string.temperature_icon),
                        modifier = Modifier.size(45.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(stringResource(R.string.temperature), fontSize = 18.sp)
                }

                Row(modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically) {

                    Row(
                        Modifier
                            .height(30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = kelvinChecked, onCheckedChange = {
                                kelvinChecked = it
                                celsiusChecked = !it
                                fahrenheitChecked=!it
                                viewModel.setTempUnit("standard")
                            })
                        Text(stringResource(R.string.kelvin), fontSize = 14.sp)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = celsiusChecked, onCheckedChange = {
                                celsiusChecked = it
                                kelvinChecked = !it
                                fahrenheitChecked=!it
                                viewModel.setTempUnit("metric")
                            })
                        Text(stringResource(R.string.celsius), fontSize = 14.sp)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = fahrenheitChecked, onCheckedChange = {
                                fahrenheitChecked = it
                                celsiusChecked = !it
                                kelvinChecked =!it
                                viewModel.setTempUnit("imperial")
                            })
                        Text(stringResource(R.string.fahrenheit), fontSize = 14.sp)
                    }
                }
            }
            ElevatedCard( //wind
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp), colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_setting_wind),
                        contentDescription = stringResource(R.string.wind_icon),
                        modifier = Modifier.size(40.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(stringResource(R.string.wind_speed), fontSize = 18.sp)
                }

                Row(modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically) {

                    Row(
                        Modifier
                            .weight(.5f)
                            .height(30.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = mileChecked, onCheckedChange = {
                                mileChecked = it
                                meterChecked = !it
                                viewModel.setWindUnit("mile")
                            })
                        Text(stringResource(R.string.miles_hour), fontSize = 14.sp)
                    }
                    Row(
                        Modifier.weight(.5f),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = meterChecked, onCheckedChange = {
                                meterChecked = it
                                mileChecked = !it
                                viewModel.setWindUnit("meter")
                            })
                        Text(stringResource(R.string.meter_sec), fontSize = 14.sp)
                    }
                }
            }

        }
    }
}