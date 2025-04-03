package com.example.skysync.helper

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.skysync.NotificationActivity
import com.example.skysync.R

class MyNotifications(private val context: Context) {
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SkySync Notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(Constants.CHANNEL_ID, name, importance).apply {
                this.description = "SkySync Weather"
                enableVibration(true)
                vibrationPattern = longArrayOf(2, 500, 200, 44)
                setSound(null, null)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    @Suppress("MissingPermission")
    fun sendNotification(isAlarm: Boolean,lat: Double,lon: Double) {
        createNotificationChannel()
        val intent = Intent(context, NotificationActivity::class.java).apply {
            putExtra(Constants.NOTIFICATION_LAT, lat)
            putExtra(Constants.NOTIFICATION_LON, lon)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT //to force update extras

        )
        val stopIntent = Intent(context, StopAlarmReceiver::class.java)
        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_setting_temp)
            .setContentTitle("SkySync")
            .setContentText("SkySync Remembering You To Check The Weather Now")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setFullScreenIntent(pendingIntent, true)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_delete, "Stop", stopPendingIntent)
        with(NotificationManagerCompat.from(context)) {
            notify(1001, builder.build())
        }
        if (isAlarm) {
            SoundManager.playAlarm(context)
        }
    }

    object SoundManager {
        private var mediaPlayer: MediaPlayer? = null
        fun playAlarm(context: Context) {
            stopAlarm()
            mediaPlayer = MediaPlayer.create(context, R.raw.alarm).apply {
                isLooping = true
                start()
            }
        }

        fun stopAlarm() {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    object PermissionHelper {
        fun checkNotificationPermission(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    activity.requestPermissions(
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        Constants.REQUEST_CODE_NOTIFICATION_PERMISSION
                    )
                }
            }
        }
    }
}