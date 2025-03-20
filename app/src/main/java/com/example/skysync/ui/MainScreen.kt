package com.example.skysync.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.example.skysync.alerts.view.AlertsScreen
import com.example.skysync.data.remote.WeatherRemoteDataSourceImp
import com.example.skysync.favorite.view.FavoriteScreen
import com.example.skysync.home.view.HomeScreen
import com.example.skysync.home.viewmodel.CurrentWeatherViewModelImp
import com.example.skysync.repo.WeatherRepositoryImp
import com.example.skysync.settings.view.SettingsScreen
import com.example.skysync.ui.navigation.BottomNavigationBar
import com.example.skysync.ui.navigation.ScreenRoute

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { Box(modifier = Modifier.height(60.dp)) { BottomNavigationBar((navController)) } }) { contentPadding ->
        val graph = navController.createGraph(startDestination = ScreenRoute.Home.route) {
            composable(route = ScreenRoute.Home.route) {
                val homeViewModel = CurrentWeatherViewModelImp(WeatherRepositoryImp(WeatherRemoteDataSourceImp.getInstance()))/* ViewModelProvider(ScreenRoute.Home, HomeViewModelFactory(
                    WeatherRepositoryImp(WeatherRemoteDataSourceImp.getInstance()))).get(
                    HomeViewModel::class.java)*/
                HomeScreen(/*navController, homeViewModel*/ )
            }
            composable(route = ScreenRoute.Favorite.route) {
                FavoriteScreen(navController)
            }
            composable(route = ScreenRoute.Alerts.route) {
                AlertsScreen(navController)
            }
            composable(route = ScreenRoute.Settings.route) {
                SettingsScreen(navController)
            }
        }
        NavHost(
            navController = navController,
            graph = graph,
            modifier = Modifier.padding(contentPadding)

        )


    }
}