package com.example.skysync.favorite.viewmodel

import com.example.skysync.models.StoredLocation

interface FavoriteViewModel {
    fun addFavoriteLocation(storedLocation: StoredLocation)
    fun deleteFavoriteLocation(storedLocation: StoredLocation)
    suspend fun getAllFavoriteLocations()
}