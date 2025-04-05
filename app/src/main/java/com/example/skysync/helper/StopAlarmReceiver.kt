package com.example.skysync.helper

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.skysync.data.local.WeatherLocalDataSourceImp
import com.example.skysync.data.remote.WeatherRemoteDataSourceImp
import com.example.skysync.repo.WeatherRepositoryImp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class StopAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.let {
            val alertIdString = intent.getStringExtra(Constants.ALERT_ID)
            if (alertIdString != null) {
                val repo = WeatherRepositoryImp(
                    WeatherRemoteDataSourceImp.getInstance(),
                    WeatherLocalDataSourceImp.getInstance(context)
                )
                val alertId = UUID.fromString(alertIdString)
                Log.d("notificationId", "BroadcastReceiver $alertId ///${alertId.javaClass.name}")
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager.cancel(alertId.hashCode())
                MyNotifications.SoundManager.stopAlarm()
                CoroutineScope(Dispatchers.IO).launch {
                    repo.deleteAlert(alertId)
                }
            }
        }
    }
}