package com.example.skysync.data

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import com.example.skysync.helper.Constants
import com.example.skysync.repo.DataStoreRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

// At the top level of your kotlin file:
private const val TAG = "Location"
class Location(private val activity: Activity,private val dataStoreRepository: DataStoreRepository){
    private lateinit var fusedClient: FusedLocationProviderClient
    /*private var stringAddress: MutableState<String> = mutableStateOf("")
    late init var locationState: MutableState<Location>*/

    suspend fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                getFreshLocation()
            } else {
                enableLocationService()
            }
        } else {
            ActivityCompat.requestPermissions(
                activity, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), Constants.REQUEST_PERMEATION_CODE
            )
        }
    }
    private fun checkPermissions(): Boolean {
        var result = false
        if (ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            result = true
        }
        return result
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            activity.getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

/*    private fun getGeoLocation(latitude: Double, longitude: Double) {
        var geoCode = Geocoder(activity  )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geoCode.getFromLocation(latitude, longitude, 1, object : GeocodeListener {
                override fun onGeocode(p0: MutableList<Address>) {
                    stringAddress.value =
                        p0[0].countryName + ", " + p0[0].adminArea + ", " + p0[0].getAddressLine(0)
                }

            })
        }
    }*/
    @SuppressLint("MissingPermission")
    private suspend fun getFreshLocation(): Pair<Double, Double> {
    var lat by mutableDoubleStateOf(0.0)
    var lon by mutableDoubleStateOf(0.0)
        fusedClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedClient.requestLocationUpdates(
            LocationRequest.Builder(0)
                .apply { setPriority(Priority.PRIORITY_HIGH_ACCURACY) }.setIntervalMillis(100000)
                .setWaitForAccurateLocation(true).build(),
            object : LocationCallback() {
                override fun onLocationResult(location: LocationResult) {
                     lat = location.locations[0].latitude
                     lon = location.locations[0].longitude
                    super.onLocationResult(location)
                    Log.i(TAG, "onLocationResult latitude: ------ $lat")
                    Log.i(TAG, "onLocationResult longitude: ------ $location")
                    CoroutineScope(Dispatchers.IO + SupervisorJob()/**/).launch {
                        addLatLongToSharedPref(lat, lon) // Pass as Double, NOT Long
                    }
                   /* locationState.value =
                        location.lastLocation ?: Location(LocationManager.GPS_PROVIDER)
                    getGeoLocation(location.locations[0].latitude, location.locations[0].longitude)*/
                }
            },
            Looper.myLooper()
        )
    return Pair(lat,lon)
    }


    private fun enableLocationService() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity.startActivity(intent)
    }

    private suspend fun addLatLongToSharedPref(lat: Double, lon: Double){
        dataStoreRepository.addLatLongToSharedPref(lat,lon)
    }
}