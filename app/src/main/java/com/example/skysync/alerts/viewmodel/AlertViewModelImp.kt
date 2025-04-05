package com.example.skysync.alerts.viewmodel

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.skysync.data.Location
import com.example.skysync.helper.Constants
import com.example.skysync.helper.MyNotifications.PermissionHelper
import com.example.skysync.helper.MyWorkManager
import com.example.skysync.helper.Response
import com.example.skysync.home.view.numEnToAr
import com.example.skysync.models.Alert
import com.example.skysync.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit

private const val TAG = "AlertViewModelImp"

class AlertViewModelImp(
    private val repository: WeatherRepository,
    private val workManager: WorkManager,
    private val location: Location,
    private val activity: Activity
) : ViewModel(),
    AlertViewModel {
    var locationLat = 0.0
    var locationLon = 0.0

    private val mutableAlertsList =
        MutableStateFlow<Response<List<Alert>>>(Response.Loading)
    val alertList: StateFlow<Response<List<Alert>>> = mutableAlertsList
    private val mutableMessage: MutableLiveData<Response<String>> = MutableLiveData()

    override fun requestNotificationPermission() {
        PermissionHelper.checkNotificationPermission(activity)
    }

    override fun setLatLon(lat: Double, lon: Double) {
        locationLat = lat
        locationLon = lon
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun addAlert(alertTime: Long, isAlarm: Boolean) {
        val currentTime = System.currentTimeMillis()
        val delay = if (alertTime > currentTime) {
            alertTime - currentTime

        } else {
            alertTime + TimeUnit.HOURS.toMillis(24) - currentTime
        }
        val dateTime=convertDateTime(alertTime)

        Log.i(TAG, "addAlert: current = $currentTime // alertTime = $alertTime //// delay = $delay")
        Log.i(TAG, "addAlert: Lat = $locationLat // Lon = $locationLon ////")
        val request =
            OneTimeWorkRequestBuilder<MyWorkManager>().addTag(Constants.MY_WORK_MANAGER_TAG)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        Constants.IS_ALARM_CODE to isAlarm,
                        Constants.NOTIFICATION_LOCATION_LAT to locationLat,
                        Constants.NOTIFICATION_LOCATION_LON to locationLon
                    )
                ).build()
        workManager.enqueue(request)
        Log.d("notificationId", "VIewModel ${request.id}  ///${request.id.javaClass.name}")

        val alert = Alert(request.id, (dateTime.first +" At "+ dateTime.second), locationLat, locationLon)
        viewModelScope.launch { addAlert(alert) }
    }

    override suspend fun addAlert(alert: Alert) {
        try {
            val locationName = withContext(Dispatchers.IO) {
                location.getGeoLocation(locationLat, locationLon)}
                alert.name = locationName
                repository.adNewAlert(alert)
            Log.i(TAG, "addAlert: $locationLat - $locationLon//$locationName")
        }catch (e: Exception){
            Log.e(TAG, "deleteAlert: ${e.message}", )
        }

    }
    override suspend fun getAllAlerts() {
        try {
            repository.getAlerts().catch { e -> mutableMessage.value = Response.Failure(e) }
                .collect { alerts -> mutableAlertsList.value = Response.Success(alerts) }
        } catch (e: Exception) {
            mutableMessage.value = Response.Failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateTime(dateLongParam : Long, lang: String="en"): Pair<String, String> {
        val dateLong = dateLongParam.toString().take(10).toLong()
        Log.i("dateTime", "Alert: $dateLong")

        val instance = Instant.ofEpochSecond(dateLong)
        val dateFormater =
            DateTimeFormatter.ofPattern("EEE, dd MMM", Locale(lang))
                .withZone(ZoneId.systemDefault())
        var formatedDate = dateFormater.format(instance)
        val timeFormatter =
            DateTimeFormatter.ofPattern("hh:mm a", Locale(lang))
                .withZone(ZoneId.systemDefault())
        var formatedTime = timeFormatter.format(instance)
        if (Locale(lang).language == "ar") {
            formatedTime = numEnToAr(formatedTime)
            formatedDate = numEnToAr(formatedDate)
            Log.i("dateTime", "Alert : $formatedDate /// $formatedTime")
        }
        return Pair(formatedDate, formatedTime)
    }
}

class AlertViewModelFactory(
    private val repository: WeatherRepository,
    private val workManager: WorkManager,
    private val location: Location,
    private val activity: Activity
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlertViewModelImp(repository, workManager, location, activity) as T
    }
}