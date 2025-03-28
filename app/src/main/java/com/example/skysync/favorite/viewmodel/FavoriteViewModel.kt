package com.example.skysync.favorite.viewmodel

import com.example.skysync.models.StoredLocation
import com.google.android.gms.maps.model.LatLng

interface FavoriteViewModel {
    suspend fun addFavoriteLocation(latLng: LatLng?)
    fun deleteFavoriteLocation(storedLocation: StoredLocation)
    suspend fun getAllFavoriteLocations()
}