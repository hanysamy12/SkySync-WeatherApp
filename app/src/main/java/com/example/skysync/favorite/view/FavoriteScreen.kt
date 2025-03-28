package com.example.skysync.favorite.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.skysync.R
import com.example.skysync.favorite.viewmodel.FavoriteViewModelImp
import com.example.skysync.helper.Response
import com.example.skysync.models.StoredLocation
import com.example.skysync.ui.navigation.ScreenRoute

@Composable
fun FavoriteScreen(viewModel: FavoriteViewModelImp, navController: NavController) {
    LaunchedEffect(Unit) {
        viewModel.getAllFavoriteLocations()
    }
    val uiFavoriteState by viewModel.favoriteList.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiFavoriteState) {
            is Response.Loading -> ProgressShow()
            is Response.Failure -> {
                val msg = (uiFavoriteState as Response.Failure).toString()
                MessageShow(msg)
            }
            is Response.Success<*> -> {
                val favoriteList = (uiFavoriteState as Response.Success).data
                FavoriteList(favoriteList) { lat, lon ->
                    navController.navigate(
                        ScreenRoute.FavoriteDetails(
                            lat,
                            lon
                        )
                    )
                }
            }
        }

    }
}

@Composable
private fun FavoriteList(
    locationsList: List<StoredLocation>,
    onFavClicked: (lat: Double, lon: Double) -> Unit
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(locationsList.size ) {
            val currentLocation = locationsList[it]
            FavoriteItem(
                currentLocation,
                onFavClicked = {onFavClicked(currentLocation.lat?:0.0,currentLocation.lon?:0.0)})
        }
    }
}

@Composable
private fun FavoriteItem(location: StoredLocation?, onFavClicked: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = .5f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                onFavClicked()
            },
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(location?.name.toString(), fontSize = 20.sp)
        Text("Egypt, fayoum", fontSize = 16.sp)
        Icon(painter = painterResource(R.drawable.ic_right_arrow), contentDescription = "Delete")
    }
}