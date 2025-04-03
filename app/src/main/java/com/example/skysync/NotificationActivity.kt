package com.example.skysync

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.skysync.data.Location
import com.example.skysync.data.local.WeatherLocalDataSourceImp
import com.example.skysync.data.remote.WeatherRemoteDataSourceImp
import com.example.skysync.helper.Constants
import com.example.skysync.helper.MyNotifications
import com.example.skysync.home.view.HomeScreen
import com.example.skysync.home.viewmodel.CurrentWeatherViewModelImp
import com.example.skysync.home.viewmodel.HomeViewModelFactory
import com.example.skysync.repo.DataStoreRepositoryImp
import com.example.skysync.repo.WeatherRepositoryImp
import com.example.skysync.ui.theme.SkySyncTheme

private const val TAG = "NotificationActivity"

class NotificationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        MyNotifications.SoundManager.stopAlarm() ////
        super.onCreate(savedInstanceState)
        val receivedLat = intent.getDoubleExtra(Constants.NOTIFICATION_LAT, 0.0)
        val receivedLon = intent.getDoubleExtra(Constants.NOTIFICATION_LON, 0.0)
        intent.removeExtra(Constants.NOTIFICATION_LAT)
        intent.removeExtra(Constants.NOTIFICATION_LON)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@NotificationActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                })
                finish()
            }  })
        Log.i(TAG, "NotificationActivity: Lat = $receivedLat /// Lon = $receivedLon")
        val homeViewModel = ViewModelProvider(
            this, HomeViewModelFactory(
                WeatherRepositoryImp(
                    WeatherRemoteDataSourceImp.getInstance(),
                    WeatherLocalDataSourceImp.getInstance(this.applicationContext)
                ),
                DataStoreRepositoryImp(this.application),
                Location(this@NotificationActivity, DataStoreRepositoryImp(this.application))
            )
        )[CurrentWeatherViewModelImp::class.java]
        setContent {
            SkySyncTheme {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    HomeScreen(homeViewModel, receivedLat, receivedLon)
                } else {
                    Log.e(TAG, "onCreate: NOTSHOWN")
                }
            }
        }
    }
}
