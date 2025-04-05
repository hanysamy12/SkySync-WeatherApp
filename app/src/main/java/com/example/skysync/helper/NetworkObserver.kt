package com.example.skysync.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NetworkObserver(context: Context) {
    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    private val mutableNetworkStatus = MutableStateFlow<NetworkStatus>(NetworkStatus.Available)
    val networkStatus: StateFlow<NetworkStatus> = mutableNetworkStatus
    init {
        val currentNetwork = connectivityManager.activeNetwork
        if (currentNetwork == null) {
            //Log.i(TAG, "onCreate: No Internet ONStart")
            mutableNetworkStatus.value = NetworkStatus.Lost

        }
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
               // Log.i("TAG", "The default network is now: $network")
                mutableNetworkStatus.value = NetworkStatus.Available

            }

            override fun onLost(network: Network) {
                //Log.i(TAG, "onCreate: No Internet Lost")
                mutableNetworkStatus.value = NetworkStatus.Lost
            }
        })
    }
}

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Lost : NetworkStatus()
}