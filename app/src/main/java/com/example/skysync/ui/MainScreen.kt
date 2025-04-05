package com.example.skysync.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.skysync.R
import com.example.skysync.TAG
import com.example.skysync.alerts.view.AlertsScreen
import com.example.skysync.alerts.viewmodel.AlertViewModelImp
import com.example.skysync.favorite.view.FavoriteDetailsScreen
import com.example.skysync.favorite.view.FavoriteScreen
import com.example.skysync.favorite.view.MapScreen
import com.example.skysync.favorite.viewmodel.FavoriteViewModelImp
import com.example.skysync.helper.Constants
import com.example.skysync.home.view.HomeScreen
import com.example.skysync.home.viewmodel.CurrentWeatherViewModelImp
import com.example.skysync.settings.view.SettingsScreen
import com.example.skysync.settings.viewmodel.SettingsViewModel
import com.example.skysync.ui.navigation.BottomNavigationBar
import com.example.skysync.ui.navigation.ScreenRoute


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    homeViewModel: CurrentWeatherViewModelImp,
    settingsViewModel: SettingsViewModel,
    favoriteViewModel: FavoriteViewModelImp,
    alertViewModelImp: AlertViewModelImp
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        bottomBar = {
            Box(modifier = Modifier.height(60.dp)) {
                BottomNavigationBar((navController))
            }
            Log.i(TAG, "MainScreen: CurrentRoute  $currentRoute")
        },
        floatingActionButton = {

            if (currentRoute == ScreenRoute.Favorite.route) FloatingActionButton(onClick = {
                navController.navigate(
                    ScreenRoute.GoogleMap(Constants.FAVORITE_SCREEN)
                )
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_heart),
                    contentDescription = "add location"
                )
            }
        },
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.Home().route,
            modifier = Modifier.padding(contentPadding)

        ) {
            composable(
                route = "home/{lat}/{lon}",
                arguments = listOf(
                    navArgument("lat") { type = NavType.StringType; nullable = true },
                    navArgument("lon") { type = NavType.StringType; nullable = true }
                )
            ) { navBackStackEntry ->
                val lat = navBackStackEntry.arguments?.getString("lat")?.toDoubleOrNull()
                val lon = navBackStackEntry.arguments?.getString("lon")?.toDoubleOrNull()
                HomeScreen(homeViewModel, lat, lon)
            }
            composable(route = ScreenRoute.Favorite.route) {
                FavoriteScreen(favoriteViewModel, navController, snackBarHostState)
            }
            composable(route = ScreenRoute.Alerts.route) {
                AlertsScreen(alertViewModelImp, navController)
            }
            composable(route = ScreenRoute.Settings.route) {
                SettingsScreen(settingsViewModel, navController)
            }
            composable<ScreenRoute.GoogleMap> { navBackStackEntry ->
                val preRoute = navController.previousBackStackEntry?.destination?.route
                val sourceScreen = when (preRoute) {
                    ScreenRoute.Favorite.route -> Constants.FAVORITE_SCREEN
                    ScreenRoute.Settings.route -> Constants.SETTINGS_SCREEN
                    ScreenRoute.Alerts.route -> Constants.ALERTS_SCREEN
                    else -> Constants.FAVORITE_SCREEN
                }
                MapScreen(
                    favoriteViewModel,
                    alertViewModelImp,
                    navController,
                    sourceScreen = sourceScreen
                )
            }
            composable<ScreenRoute.FavoriteDetails> { navBackStackEntry ->
                val data = navBackStackEntry.toRoute<ScreenRoute.FavoriteDetails>()
                FavoriteDetailsScreen(homeViewModel, data.lat, data.lon)
            }
        }

    }
}
