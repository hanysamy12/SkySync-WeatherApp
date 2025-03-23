package com.example.skysync

import androidx.datastore.preferences.core.longPreferencesKey

class Constants{
    companion object{
        const val REQUEST_PERMEATION_CODE = 1999
        const val LOCATION_DATASTORE_NAME = "location_store"
        /*const val CURRENT_LAT_KEY = "current_lat"
        const val CURRENT_LON_KEY = "current_lon"*/
        val CURRENT_LAT_KEY = longPreferencesKey("current_latitude")
        val CURRENT_LON_KEY = longPreferencesKey("current_longitude")
    }
}