package com.example.skysync.helper

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.skysync.data.local.WeatherLocalDataSourceImp
import com.example.skysync.data.remote.WeatherRemoteDataSourceImp
import com.example.skysync.repo.WeatherRepositoryImp
import java.util.UUID

private const val TAG = "MyWorkManager"

class MyWorkManager(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
            Log.i(TAG, "doWork: Worker/////////")
            val alertId = id
            val isAlarm = inputData.getBoolean(Constants.IS_ALARM_CODE, false)
            val lat = inputData.getDouble(Constants.NOTIFICATION_LOCATION_LAT, 0.0)
            val lon = inputData.getDouble(Constants.NOTIFICATION_LOCATION_LON, 0.0)
            val notification = MyNotifications(context)
            notification.sendNotification(alertId,isAlarm, lat, lon)
            Log.d("notificationId", "WorkManager $id  ///${id.javaClass.name}")
            return Result.success()

        } catch (e: Exception) {
            return Result.failure(
                workDataOf(
                    "ERROR_MSG" to e.message,
                    "STACKTRACE" to Log.getStackTraceString(e)
                )
            )
        }

    }
}

