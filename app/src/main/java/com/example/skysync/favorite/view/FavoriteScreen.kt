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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.skysync.R
import com.example.skysync.favorite.viewmodel.FavoriteViewModelImp
import com.example.skysync.helper.Response
import com.example.skysync.home.view.MessageShow
import com.example.skysync.home.view.ProgressShow
import com.example.skysync.models.StoredLocation
import com.example.skysync.ui.navigation.ScreenRoute
import kotlinx.coroutines.launch

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModelImp,
    navController: NavController,
    snackBarHostState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()
    var locationToDelete by remember { mutableStateOf<StoredLocation?>(null) }
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
                FavoriteList(locationsList = favoriteList, onFavClicked = { lat, lon ->
                    navController.navigate(
                        ScreenRoute.FavoriteDetails(
                            lat,
                            lon
                        )
                    )
                }, onDeleteClicked = {location ->
                    coroutineScope.launch {
                        locationToDelete = location
                        val result = snackBarHostState.showSnackbar(
                            message = "Location deleted",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            locationToDelete = null
                        }else {
                            locationToDelete?.let { viewModel.deleteFavoriteLocation(it) }
                        }
                    }
                })
            }
        }

    }
}

@Composable
private fun FavoriteList(
    locationsList: List<StoredLocation>,
    onFavClicked: (lat: Double, lon: Double) -> Unit,
    onDeleteClicked: (StoredLocation) -> Unit
) {

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                stringResource(R.string.favorite),
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        items(locationsList.size) {
            val currentLocation = locationsList[it]
            FavoriteItem(
                currentLocation,
                onFavClicked = {
                    onFavClicked(
                        currentLocation.lat ?: 0.0,
                        currentLocation.lon ?: 0.0
                    )
                }, onDeleteClicked = { onDeleteClicked(currentLocation) })
        }
    }
}

@Composable
private fun FavoriteItem(
    location: StoredLocation?,
    onFavClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
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
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            location?.name.toString(),
            fontSize = 18.sp,
            maxLines = 2,
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(.8f)
        )
        IconButton(
            onClick = { onDeleteClicked() },
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = "Delete",
                modifier = Modifier
                    .padding(end = 12.dp)
                    .weight(.2f)
            )
        }
    }
}