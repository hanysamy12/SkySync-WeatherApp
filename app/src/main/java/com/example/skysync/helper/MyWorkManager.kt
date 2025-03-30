package com.example.skysync.helper

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

private const val TAG = "MyWorkManager"

class MyWorkManager(private val context: Context,/*private val activity: Activity,*/ workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
            // delay(200)
            Log.i(TAG, "doWork: Worker/////////")
            val notification = MyNotifications(context )
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

