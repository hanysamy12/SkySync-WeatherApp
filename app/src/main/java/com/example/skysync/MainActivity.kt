package com.example.skysync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skysync.alerts.view.AlertsScreen
import com.example.skysync.data.remote.WeatherRemoteDataSourceImp
import com.example.skysync.favorite.view.FavoriteScreen
import com.example.skysync.home.view.HomeScreen
import com.example.skysync.home.viewmodel.CurrentWeatherViewModelImp
import com.example.skysync.home.viewmodel.HomeViewModelFactory
import com.example.skysync.repo.WeatherRepositoryImp
import com.example.skysync.settings.view.SettingsScreen
import com.example.skysync.ui.navigation.BottomNavigationBar
import com.example.skysync.ui.navigation.ScreenRoute
import com.example.skysync.ui.theme.SkySyncTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        val homeViewModel = ViewModelProvider(this, HomeViewModelFactory(WeatherRepositoryImp(
            WeatherRemoteDataSourceImp.getInstance())))[CurrentWeatherViewModelImp::class.java]
        setContent {
            SkySyncTheme {
                MainScreen(homeViewModel)
            }

        }
    }
}

@Composable
fun MainScreen(homeViewModel: CurrentWeatherViewModelImp) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Box(modifier = Modifier.height(60.dp)) {
                BottomNavigationBar((navController))
            }
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.Home.route,
            modifier = Modifier.padding(contentPadding)

        ) {
            composable(route = ScreenRoute.Home.route) {
                HomeScreen( homeViewModel)
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


    }
}

