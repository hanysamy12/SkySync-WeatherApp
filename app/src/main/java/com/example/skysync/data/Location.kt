package com.example.skysync.data

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val TAG = "Location"

class Location(
    private val activity: Activity,
    private val dataStoreRepository: DataStoreRepository
) {


    suspend fun getLocation() : Pair<Double, Double> {
      return (if (checkPermissions()) {
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
      }) as Pair<Double, Double>
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

    @SuppressLint("MissingPermission")
    private suspend fun getFreshLocation(): Pair<Double, Double> = withContext(Dispatchers.IO) {
        val fusedClient = LocationServices.getFusedLocationProviderClient(activity)

        suspendCoroutine { continuation ->
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    fusedClient.removeLocationUpdates(this)
                    val location = locationResult.lastLocation ?: run {
                        continuation.resumeWithException(Exception("Null location received"))
                        return
                    }
                    launch {
                        addLatLongToSharedPref(location.latitude, location.longitude)
                    }
                    continuation.resume(Pair(location.latitude, location.longitude))
                    Log.i(TAG, "onLocationResult: $location")
                }
            }


            fusedClient.requestLocationUpdates(
                LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 0)
                    .setWaitForAccurateLocation(true)
                    .build(),
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }



    private suspend fun enableLocationService() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity.startActivity(intent)
        getLocation() ////////
    }

    private suspend fun addLatLongToSharedPref(lat: Double, lon: Double) {
        dataStoreRepository.addLatLongToSharedPref(lat, lon)
    }

    suspend fun getGeoLocation(latitude: Double, longitude: Double): String = withContext(
        Dispatchers.IO
    ) {
        var geoCode = Geocoder(activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return@withContext suspendCoroutine { cont ->
                geoCode.getFromLocation(latitude, longitude, 1, object : GeocodeListener {
                    override fun onGeocode(p0: MutableList<Address>) {
                        cont.resume(p0[0].countryName + ", " + p0[0].adminArea)

                    }

                })
            }
        } else {
            val addresses = geoCode.getFromLocation(latitude, longitude, 1)
            return@withContext "${addresses?.get(0)?.countryName}"/*, ${addresses?.get(0)?.adminArea}*/
        }
    }
}

