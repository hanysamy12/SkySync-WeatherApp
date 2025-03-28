package com.example.skysync.favorite.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.skysync.favorite.viewmodel.FavoriteViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun MapScreen(viewModel: FavoriteViewModel, navController: NavController) {
    var lat by remember { mutableDoubleStateOf(29.394835) }
    var lon by remember { mutableDoubleStateOf(30.902915) }
    val cameraPositionState = rememberCameraPositionState()
    var clickedPosition by remember { mutableStateOf<LatLng?>(null) }
    var buttonEnabled by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
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
            },
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                compassEnabled = true

            )
        ) {
            Marker(
                state = MarkerState(position = LatLng(lat, lon)),
                title = "Current Weather",
                snippet = "Tap for details"
            )

        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ElevatedButton(
                onClick = {
                    coroutineScope.launch {viewModel.addFavoriteLocation(clickedPosition) }
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
    }

}


/*ShowChosenLocation("Cairo", onAddClick = {viewModel.addFavoriteLocation(location)
                navController.popBackStack()})*/
/*

@Preview
@Composable
private fun ShowNoDate() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.no_location_selected), fontSize = 20.sp, color = Color.White)
    }
}
*/
/*@Composable
private fun ShowChosenLocation(latLng: LatLng, onAddClick: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("$latLng", fontSize = 20.sp, color = Color.White)
        ElevatedButton(
            onClick = { onAddClick() },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White
            ),
            //  contentPadding = TODO(),

        ) {
            Text(stringResource(R.string.add_to_favorite), Modifier.background(Color.Transparent))
        }
    }
}*/
