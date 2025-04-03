package com.example.skysync.alerts.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.skysync.helper.Constants
import com.example.skysync.helper.MyNotifications
import com.example.skysync.helper.MyNotifications.PermissionHelper
import com.example.skysync.helper.MyWorkManager
import com.google.android.gms.maps.model.LatLng
import java.util.concurrent.TimeUnit

private const val TAG = "AlertViewModelImp"

class AlertViewModelImp(
    private val myNotifications: MyNotifications,
    private val workManager: WorkManager,
    private val activity: Activity
) : ViewModel(),
    AlertViewModel {
    var locationLat = 0.0
    var locationLon = 0.0
    override fun requestNotificationPermission() {
        PermissionHelper.checkNotificationPermission(activity)
    }

    override fun setLatLon(lat: Double,lon: Double) {
        locationLat = lat
        locationLon = lat
    }
    override fun addAlert(alertTime: Long, isAlarm: Boolean) {
        val currentTime = System.currentTimeMillis()
        val delay = if (alertTime > currentTime) {
            alertTime - currentTime
        } else {
            alertTime + TimeUnit.HOURS.toMillis(24) - currentTime
        }
        Log.i(TAG, "addAlert: current = $currentTime // alertTime = $alertTime //// delay = $delay")
        Log.i(TAG, "addAlert: Lat = $locationLat // Lon = $locationLon ////")
        val request =
            OneTimeWorkRequestBuilder<MyWorkManager>().addTag(Constants.MY_WORK_MANAGER_TAG)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(Constants.IS_ALARM_CODE to isAlarm, Constants.NOTIFICATION_LOCATION_LAT to locationLat,
                        Constants.NOTIFICATION_LOCATION_LON to locationLon)
                ).build()
        workManager.enqueue(request)
    }

}

class AlertViewModelFactory(
    private val myNotifications: MyNotifications,
    private val workManager: WorkManager,
    private val activity: Activity
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlertViewModelImp(myNotifications, workManager, activity) as T
    }
}