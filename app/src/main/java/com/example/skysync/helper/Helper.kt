package com.example.skysync.helper

import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

class Constants {
    companion object {
        const val REQUEST_PERMEATION_CODE = 1999  ///location class
        const val LOCATION_DATASTORE_NAME = "location_store"
        const val SETTINGS_DATASTORE_NAME = "settings_store"
        val CURRENT_LAT_KEY = doublePreferencesKey("current_latitude")
        val CURRENT_LON_KEY = doublePreferencesKey("current_longitude")
        val LANGUAGE_KEY = stringPreferencesKey("selected_language")
        val LOCATION_WAY_KEY = stringPreferencesKey("location_way")
        val TEMPERATURE_UNIT = stringPreferencesKey("temperature_measure")
        val WIND_SPEED_UNIT = stringPreferencesKey("wind_speed_measure")

        //settings
        const val SETTINGS_LANGUAGE = "language_setting"
        const val SETTINGS_LOCATION = "location_setting"
        const val SETTINGS_TEMP = "temp_settings"
        const val SETTINGS_WIND = "wind_settings"

        //work manager
        const val MY_WORK_MANAGER_TAG = "notification_work_manager"
        const val CHANNEL_ID ="notification_channel_id"
        const val REQUEST_CODE_NOTIFICATION_PERMISSION = 1000
        const val IS_ALARM_CODE = "is_alarm"
    }
}

sealed class Response<out T> {
    data object Loading : Response<Nothing>()
    data class Success<T>(val data: T) : Response<T>()
    data class Failure(val error: Throwable) : Response<Nothing>()
}