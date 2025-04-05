package com.example.skysync

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import androidx.work.WorkManager
import com.example.skysync.alerts.view.AlertsScreen
import com.example.skysync.alerts.viewmodel.AlertViewModelFactory
import com.example.skysync.alerts.viewmodel.AlertViewModelImp
import com.example.skysync.data.Location
import com.example.skysync.data.local.WeatherLocalDataSourceImp
import com.example.skysync.data.remote.WeatherRemoteDataSourceImp
import com.example.skysync.favorite.view.FavoriteDetailsScreen
import com.example.skysync.favorite.view.FavoriteScreen
import com.example.skysync.favorite.view.MapScreen
import com.example.skysync.favorite.viewmodel.FavoriteViewModelImp
import com.example.skysync.favorite.viewmodel.FavoriteViwModelFactory
import com.example.skysync.helper.Constants
import com.example.skysync.home.view.HomeScreen
import com.example.skysync.home.viewmodel.CurrentWeatherViewModelImp
import com.example.skysync.home.viewmodel.HomeViewModelFactory
import com.example.skysync.repo.DataStoreRepository
import com.example.skysync.repo.DataStoreRepositoryImp
import com.example.skysync.repo.WeatherRepositoryImp
import com.example.skysync.settings.view.SettingsScreen
import com.example.skysync.settings.viewmodel.SettingsViewModel
import com.example.skysync.settings.viewmodel.SettingsViewModelFactory
import com.example.skysync.settings.viewmodel.SettingsViewModelImp
import com.example.skysync.ui.MainScreen
import com.example.skysync.ui.navigation.BottomNavigationBar
import com.example.skysync.ui.navigation.ScreenRoute
import com.example.skysync.ui.theme.SkySyncTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.Locale

internal const val TAG = "MainActivity"


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStoreRepo = DataStoreRepositoryImp(this.application)
        lifecycleScope.launch {
            changeLocal(dataStoreRepo, this@MainActivity)
        }
        /////ViewModels
        val homeViewModel = ViewModelProvider(
            this, HomeViewModelFactory(
                WeatherRepositoryImp(
                    WeatherRemoteDataSourceImp.getInstance(),
                    WeatherLocalDataSourceImp.getInstance(this.applicationContext)
                ),
                DataStoreRepositoryImp(this.application),
                Location(this@MainActivity, DataStoreRepositoryImp(this.application))
            )
        )[CurrentWeatherViewModelImp::class.java]

        val settingsViewModel = ViewModelProvider(
            this, SettingsViewModelFactory(DataStoreRepositoryImp(this.application))
        )[SettingsViewModelImp::class.java]

        val favoriteViewModel = ViewModelProvider(
            this, FavoriteViwModelFactory(
                WeatherRepositoryImp(
                    WeatherRemoteDataSourceImp.getInstance(),
                    WeatherLocalDataSourceImp.getInstance(this.applicationContext)
                ), Location(this@MainActivity, DataStoreRepositoryImp(this.application))
            )
        )[FavoriteViewModelImp::class.java]

        val alertViewModel = ViewModelProvider(
            this, AlertViewModelFactory(
                WeatherRepositoryImp(
                    WeatherRemoteDataSourceImp.getInstance(),
                    WeatherLocalDataSourceImp.getInstance(this.applicationContext)
                ),
                WorkManager.getInstance(this@MainActivity),
                Location(this@MainActivity, DataStoreRepositoryImp(this.application)),
                this@MainActivity
            )
        )[AlertViewModelImp::class.java]
        setContent {
            SkySyncTheme {
                MainScreen(homeViewModel, settingsViewModel, favoriteViewModel, alertViewModel)

            }

        }
    }

    suspend fun changeLocal(dateStoreRepository: DataStoreRepository, context: Context) {
        dateStoreRepository.getLanguage().distinctUntilChanged().collect {
            Log.i(TAG, "changeLocal: $it")
            val locale = Locale(it)
            Locale.setDefault(locale)
            val config = context.resources.configuration
            config.setLocale(locale)
            config.setLayoutDirection(locale)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            delay(600)
            //recreate()
        }
    }

}

