package com.example.skysync.helper

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.util.Calendar

private const val TAG = "MyWorkManager"

class MyWorkManager(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
                // delay(200)
                Log.i(TAG, "doWork: Worker/////////")
                val notification = MyNotifications(context)
                notification.sendNotification()
                return Result.success()

        } catch (e: Exception) {
            return Result.failure(
                workDataOf(
                    //"ERROR_CODE" to e.message
                )
            )
        }
    }
    private fun isWithinTimeRange(startHour: Int ,statMinuit :Int, endHour: Int, endMinuit: Int): Boolean {
        val now = Calendar.getInstance()
        val currentHour = now.get(Calendar.HOUR_OF_DAY)
        val currentMinute = now.get(Calendar.MINUTE)

        val currentTime = currentHour * 60 + currentMinute
        val startTime = startHour * 60 + statMinuit
        val endTime = endHour * 60 + endMinuit

        return currentTime in startTime .. endTime
    }
}

