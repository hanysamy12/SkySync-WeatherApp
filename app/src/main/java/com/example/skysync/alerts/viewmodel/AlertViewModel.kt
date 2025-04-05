package com.example.skysync.alerts.viewmodel

import com.example.skysync.models.Alert

interface AlertViewModel {
    fun requestNotificationPermission()
    fun addAlert(alertTime:Long,isAlarm: Boolean)
    fun setLatLon(lat: Double,lon: Double)
    suspend fun addAlert(alert: Alert)
    suspend fun getAllAlerts()
}