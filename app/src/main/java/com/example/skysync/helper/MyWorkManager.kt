package com.example.skysync.helper

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

private const val TAG = "MyWorkManager"

class MyWorkManager(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
            // delay(200)
            Log.i(TAG, "doWork: Worker/////////")
            val notification = MyNotifications(context)
           notification.createNotificationChannel()
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
}

