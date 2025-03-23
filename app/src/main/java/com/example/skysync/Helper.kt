package com.example.skysync

import androidx.datastore.preferences.core.longPreferencesKey

class Constants{
    companion object{
        const val REQUEST_PERMEATION_CODE = 1999
        const val LOCATION_DATASTORE_NAME = "location_store"
        val CURRENT_LAT_KEY = longPreferencesKey("current_latitude")
        val CURRENT_LON_KEY = longPreferencesKey("current_longitude")

    }
}
sealed class Response<out T>{
    data object Loading : Response<Nothing>()
    data class Success<T> (val data : T): Response<T>()
    data class Failure (val error: Throwable): Response<Nothing>()
}