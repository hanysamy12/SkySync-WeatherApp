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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skysync.R
import com.example.skysync.settings.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    //language
    var englishChecked by remember { mutableStateOf(true) }
    var arabicChecked by remember { mutableStateOf(false) }
    //location
    var gpsChecked by remember { mutableStateOf(true) }
    var mapChecked by remember { mutableStateOf(false) }
    //speed
    var meterChecked by remember { mutableStateOf(true) }
    var mileChecked by remember { mutableStateOf(false) }
    //temp
    var kelvinChecked by remember { mutableStateOf(true) }
    var celsiusChecked by remember { mutableStateOf(false) }
    var fahrenheitChecked by remember { mutableStateOf(false) }

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
                        contentDescription = "language Icon",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("Language", fontSize = 18.sp)
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
                        Text("English", fontSize = 14.sp) ///Fixed
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
                        Text("عربي", fontSize = 14.sp) ///Fixed
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
                        contentDescription = "location Icon",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("Location", fontSize = 18.sp)
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
                        Text("Gps", fontSize = 14.sp)
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
                        Text("map", fontSize = 14.sp)
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
                        contentDescription = "language Icon",
                        modifier = Modifier.size(45.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("Temperature", fontSize = 18.sp)
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
                        Text("Kelvin", fontSize = 14.sp)
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
                        Text("Celsius", fontSize = 14.sp)
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
                        Text("Fahrenheit", fontSize = 14.sp)
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
                        contentDescription = "wind Icon",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("WindSpeed", fontSize = 18.sp)
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
                                viewModel.setWindUnit("miles")
                            })
                        Text("miles/hour", fontSize = 14.sp)
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
                        Text("meter/sec", fontSize = 14.sp)
                    }
                }
            }

        }
    }
}