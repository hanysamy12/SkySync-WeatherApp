package com.example.skysync.favorite.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skysync.data.Location
import com.example.skysync.helper.Response
import com.example.skysync.models.StoredLocation
import com.example.skysync.repo.WeatherRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModelImp(private val weatherRepository: WeatherRepository,private val location: Location) : ViewModel(),
    FavoriteViewModel {
    private val mutableFavoriteList =
        MutableStateFlow<Response<List<StoredLocation>>>(Response.Loading)
    val favoriteList: StateFlow<Response<List<StoredLocation>>> = mutableFavoriteList
    private val mutableMessage : MutableLiveData<Response<String>> = MutableLiveData()
    override suspend fun addFavoriteLocation(latLng: LatLng?) {
        val locationName =location.getGeoLocation(latLng?.latitude?:0.0,latLng?.longitude?:0.0)
        val storedLocation = StoredLocation(lat = latLng?.latitude, lon = latLng?.longitude, name = locationName)
        viewModelScope.launch {
            weatherRepository.adNewFavoriteLocations(storedLocation)
        }
    }

    override fun deleteFavoriteLocation(storedLocation: StoredLocation) {
        viewModelScope.launch {
            weatherRepository.deleteFavoriteLocation(storedLocation)
        }
    }

    override suspend fun getAllFavoriteLocations() {
        try {
            weatherRepository.getFavoriteLocations()
                .catch { e -> mutableMessage.value = Response.Failure(e) }
                .collect { locations ->
                    mutableFavoriteList.value = Response.Success(locations)
                }
        } catch (e: Exception) {
            mutableMessage.value = Response.Failure(e)
        }
    }
}

class FavoriteViwModelFactory(
    private val repo: WeatherRepository,private val location: Location
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteViewModelImp(repo, location ) as T
    }
}