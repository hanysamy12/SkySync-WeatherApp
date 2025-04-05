package com.example.skysync.favorite.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skysync.R
import com.example.skysync.alerts.viewmodel.AlertViewModel
import com.example.skysync.favorite.viewmodel.FavoriteViewModelImp
import com.example.skysync.helper.Constants
import com.example.skysync.models.SearchLocationsResponseItem
import com.example.skysync.ui.navigation.ScreenRoute
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch


private const val TAG = "MapScreen"

@Composable
fun MapScreen(
    viewModel: FavoriteViewModelImp,
    alertViewModel: AlertViewModel,
    navController: NavController,
    sourceScreen: Int
) {
    var lat by remember { mutableDoubleStateOf(29.394835) }
    var lon by remember { mutableDoubleStateOf(30.902915) }
    val cameraPositionState = rememberCameraPositionState()
    var clickedPosition by remember { mutableStateOf<LatLng?>(null) }
    var buttonEnabled by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    val locations by viewModel.searchResult.collectAsState()
    var locationSelected by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true,
                mapType = MapType.NORMAL
            ), onMapClick = { latLon ->
                clickedPosition = latLon
                buttonEnabled = true
                lat = latLon.latitude
                lon = latLon.longitude
                Log.i(TAG, "MapScreen: ${latLon.latitude } //// ${latLon.longitude}")
            },
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                compassEnabled = true
            )
        ) {
            Marker(
                state = MarkerState(position = LatLng(lat, lon)),
                title = stringResource(R.string.current_location),
            )

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                OutlinedTextField(
                    value = searchQuery, onValueChange = { query ->
                        viewModel.updateQuery(query)
                        searchQuery = query
                        locationSelected = false
                     //   Log.i(TAG, "MapScreen: $query")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    label = { Text(stringResource(R.string.search)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                    },
                    singleLine = true
                )
                if (!locationSelected) {
                    if (locations.isNotEmpty()) {
                        Column(
                            Modifier
                                .background(
                                    MaterialTheme.colorScheme.secondary.copy(.8f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(vertical = 5.dp)
                                .fillMaxWidth()
                        ) {
                            Log.e(TAG, "MapScreen: ${locations::class.simpleName}")
                            locations.forEach { location ->
                                Log.i(TAG, "MapScreen: $location")
                                SearchResultItem(location) { selectedLat, selectedLon ->
                                    lat = selectedLat
                                    lon = selectedLon
                                    clickedPosition = LatLng(selectedLat, selectedLon)
                                    buttonEnabled = true
                                    locationSelected = true
                                }
                            }
                        }
                    }
                }
            }
            if (sourceScreen == Constants.FAVORITE_SCREEN) {
                ElevatedButton(
                    onClick = {
                        coroutineScope.launch { viewModel.addFavoriteLocation(clickedPosition) }
                        navController.popBackStack()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White
                    ),
                    enabled = buttonEnabled
                ) {
                    Text(
                        stringResource(R.string.add_to_favorite),
                        Modifier.background(Color.Transparent)
                    )
                }
            }
            else if (sourceScreen == Constants.SETTINGS_SCREEN) {
                ElevatedButton(
                    onClick = {
                        navController.navigate("home/${lat}/${lon}") {
                            popUpTo(ScreenRoute.Home().route) {
                                inclusive = false
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White
                    ),
                    enabled = buttonEnabled
                ) {
                    Text(
                        stringResource(R.string.go_to_home),
                        Modifier.background(Color.Transparent)
                    )
                }
            }
            else if (sourceScreen == Constants.ALERTS_SCREEN) {
                ElevatedButton(
                    onClick = {
                        navController.popBackStack()
                        alertViewModel.setLatLon(lat ,lon)

                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White
                    ),
                    enabled = buttonEnabled
                ) {
                    Text(
                        stringResource(R.string.set_location),
                        Modifier.background(Color.Transparent)
                    )
                }
            }

        }
    }
}

@Composable
fun SearchResultItem(
    location: SearchLocationsResponseItem,
    onLocationClicked: (Double, Double) -> Unit
) {
    Row(modifier = Modifier
        .padding(16.dp)
        .clickable {
            onLocationClicked(location.lat as Double, location.lon as Double)
        }) {
        Text(text = "${location.country},${location.state} ${location.name} : ")
    }
}