package com.example.skysync.favorite.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skysync.R
import com.example.skysync.favorite.viewmodel.FavoriteViewModel
import com.example.skysync.models.StoredLocation
import com.example.skysync.ui.navigation.ScreenRoute
import com.example.skysync.ui.theme.color3

@Composable
fun MapScreen(viewModel: FavoriteViewModel,navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = color3)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.onBackground, RoundedCornerShape(24.dp))
                , verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
            ) {
                val location = StoredLocation(lat = 44.876, lon =-73.5678, name = "Chickadee")
                ShowChosenLocation("Cairo", onAddClick = {viewModel.addFavoriteLocation(location)
                navController.popBackStack()})
            }
        }
    }
}

@Preview
@Composable
private fun ShowNoDate() {
    Column(
        Modifier.fillMaxWidth().background(Color.Transparent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.no_location_selected), fontSize = 20.sp, color = Color.White)
    }
}

@Composable
private fun ShowChosenLocation(cityName : String,onAddClick:()-> Unit) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(cityName, fontSize = 20.sp, color = Color.White)
        ElevatedButton(
            onClick = {onAddClick()},
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
}