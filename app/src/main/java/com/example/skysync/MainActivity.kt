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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.skysync.alerts.view.AlertsScreen
import com.example.skysync.data.Location
import com.example.skysync.data.local.WeatherLocalDataSourceImp
import com.example.skysync.data.remote.WeatherRemoteDataSourceImp
import com.example.skysync.favorite.view.FavoriteDetailsScreen
import com.example.skysync.favorite.view.FavoriteScreen
import com.example.skysync.favorite.view.MapScreen
import com.example.skysync.favorite.viewmodel.FavoriteViewModelImp
import com.example.skysync.favorite.viewmodel.FavoriteViwModelFactory
import com.example.skysync.helper.Constants
import com.example.skysync.helper.MyWorkManager
import com.example.skysync.helper.PermissionHelper
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
import com.example.skysync.ui.navigation.BottomNavigationBar
import com.example.skysync.ui.navigation.ScreenRoute
import com.example.skysync.ui.theme.SkySyncTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit

private const val TAG = "MainActivity"


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //////////
       // PermissionHelper.checkNotificationPermission(this)
        ////////
        val workManager = WorkManager.getInstance(this@MainActivity)
        val request =
            OneTimeWorkRequestBuilder<MyWorkManager>().addTag(Constants.MY_WORK_MANAGER_TAG)
                .setInitialDelay(10,TimeUnit.SECONDS).build()
        workManager.enqueue(request)
        /*  workManager.getWorkInfoByIdLiveData(request.id).observe(this, Observer { workInfo ->
              when (workInfo?.state) {
                  WorkInfo.State.SUCCEEDED -> {
                      Log.i(TAG, "Main Got : $workInfo")
                      try {

                      } catch (e: IOException) {
                          Log.e(TAG, "Got From File Error: $e")
                      }
                  }
                  else -> {
                      Log.e(TAG, "Main Failed ${workInfo?.state}")
                  }
              }
          })*/

        ////////////
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
        setContent {
            SkySyncTheme {
                MainScreen(homeViewModel, settingsViewModel, favoriteViewModel)

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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    homeViewModel: CurrentWeatherViewModelImp,
    settingsViewModel: SettingsViewModel,
    favoriteViewModel: FavoriteViewModelImp
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        Box(modifier = Modifier.height(60.dp)) {
            BottomNavigationBar((navController))
        }
        Log.i(TAG, "MainScreen: CurrentRoute  $currentRoute")
    }, floatingActionButton = {

        if (currentRoute == ScreenRoute.Favorite.route) FloatingActionButton(onClick = {
            navController.navigate(
                ScreenRoute.GoogleMap
            )
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_heart),
                contentDescription = "add location"
            )
        }
    }

    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.Home.route,
            modifier = Modifier.padding(contentPadding)

        ) {
            composable(route = ScreenRoute.Home.route) {
                HomeScreen(homeViewModel)
            }
            composable(route = ScreenRoute.Favorite.route) {
                FavoriteScreen(favoriteViewModel, navController)
            }
            composable(route = ScreenRoute.Alerts.route) {
                AlertsScreen(navController)
            }
            composable(route = ScreenRoute.Settings.route) {
                SettingsScreen(settingsViewModel)
            }
            composable<ScreenRoute.GoogleMap> {
                MapScreen(favoriteViewModel, navController)
            }
            composable<ScreenRoute.FavoriteDetails> { navBackStackEntry ->
                val data = navBackStackEntry.toRoute<ScreenRoute.FavoriteDetails>()
                FavoriteDetailsScreen(homeViewModel, data.lat, data.lon)
            }
        }
    }
}

