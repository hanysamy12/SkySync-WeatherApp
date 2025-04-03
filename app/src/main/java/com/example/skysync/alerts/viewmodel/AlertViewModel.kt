package com.example.skysync.alerts.viewmodel

import com.google.android.gms.maps.model.LatLng

interface AlertViewModel {
    fun requestNotificationPermission()
    fun addAlert(alertTime:Long,isAlarm: Boolean)
    fun setLatLon(lat: Double,lon: Double)
}