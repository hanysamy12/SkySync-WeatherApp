package com.example.skysync

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.skysync.data.Location
import com.example.skysync.data.local.WeatherLocalDataSourceImp
import com.example.skysync.data.remote.WeatherRemoteDataSourceImp
import com.example.skysync.helper.Constants
import com.example.skysync.helper.MyNotifications
import com.example.skysync.helper.NetworkObserver
import com.example.skysync.home.view.HomeScreen
import com.example.skysync.home.viewmodel.CurrentWeatherViewModelImp
import com.example.skysync.home.viewmodel.HomeViewModelFactory
import com.example.skysync.repo.DataStoreRepositoryImp
import com.example.skysync.repo.WeatherRepositoryImp
import com.example.skysync.ui.theme.SkySyncTheme
import kotlinx.coroutines.launch
import java.util.UUID


class NotificationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        MyNotifications.SoundManager.stopAlarm()
        super.onCreate(savedInstanceState)

        val receivedLat = intent.getDoubleExtra(Constants.NOTIFICATION_LAT, 0.0)
        val receivedLon = intent.getDoubleExtra(Constants.NOTIFICATION_LON, 0.0)

        val rep = WeatherRepositoryImp(
            WeatherRemoteDataSourceImp.getInstance(),
            WeatherLocalDataSourceImp.getInstance(this)
        )
       val alertId = intent.getStringExtra(Constants.ALERT_ID)
        lifecycleScope.launch {
            rep.deleteAlert(UUID.fromString(alertId))
          //  Log.d("notificationId", "Notification Activity ${UUID.fromString(alertId)}  ///${UUID.fromString(alertId).javaClass.name}")
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@NotificationActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                })
                finish()
            }
        })
        Log.i("Alert", "NotificationActivity: Lat = $receivedLat /// Lon = $receivedLon")
        val homeViewModel = ViewModelProvider(
            this, HomeViewModelFactory(
                WeatherRepositoryImp(
                    WeatherRemoteDataSourceImp.getInstance(),
                    WeatherLocalDataSourceImp.getInstance(this.applicationContext)
                ),
                DataStoreRepositoryImp(this.application),
                Location(this@NotificationActivity, DataStoreRepositoryImp(this.application)),
                NetworkObserver(this)
            )
        )[CurrentWeatherViewModelImp::class.java]
        setContent {
            SkySyncTheme {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    HomeScreen(homeViewModel, receivedLat, receivedLon)
                } else {
                    Log.e(TAG, "onCreate: ")
                }
            }
        }
    }
}
