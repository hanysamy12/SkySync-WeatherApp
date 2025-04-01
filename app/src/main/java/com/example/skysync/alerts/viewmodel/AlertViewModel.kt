package com.example.skysync.alerts.viewmodel

interface AlertViewModel {
    fun requestNotificationPermission()
    fun addAlert(alertTime:Long)
}