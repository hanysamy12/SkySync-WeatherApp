package com.example.skysync.helper

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StopAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.let {
            val notificationManager = it.getSystemService(NotificationManager::class.java)
            notificationManager.cancel(1001)
            MyNotifications.SoundManager.stopAlarm()
        }
    }
}