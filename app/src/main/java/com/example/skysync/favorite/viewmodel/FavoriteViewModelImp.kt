package com.example.skysync.favorite.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skysync.data.Location
import com.example.skysync.helper.Response
import com.example.skysync.models.SearchLocationsResponseItem
import com.example.skysync.models.StoredLocation
import com.example.skysync.repo.WeatherRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "FavoriteViewModelImp"

class FavoriteViewModelImp(
    private val weatherRepository: WeatherRepository,
    private val location: Location
) : ViewModel(),
    FavoriteViewModel {

    private val mutableFavoriteList =
        MutableStateFlow<Response<List<StoredLocation>>>(Response.Loading)
    val favoriteList: StateFlow<Response<List<StoredLocation>>> = mutableFavoriteList
    private val mutableMessage: MutableLiveData<Response<String>> = MutableLiveData()
    override suspend fun addFavoriteLocation(latLng: LatLng?) {
        val locationName =
            location.getGeoLocation(latLng?.latitude ?: 0.0, latLng?.longitude ?: 0.0)
        val storedLocation =
            StoredLocation(lat = latLng?.latitude, lon = latLng?.longitude, name = locationName)
        Log.i(TAG, "addFavoriteLocation: $storedLocation")
        viewModelScope.launch {
            weatherRepository.adNewFavoriteLocations(storedLocation)
        }
    }

    private val mutableSearchQuery = MutableStateFlow("")
    private val mutableSearchFlow = MutableSharedFlow<String>(replay = 1)
    val searchQuery: StateFlow<String> = mutableSearchQuery

    override fun updateQuery(query: String) {
        mutableSearchQuery.value = query
        viewModelScope.launch { mutableSearchFlow.emit(query) }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResult : StateFlow<List<SearchLocationsResponseItem>> = mutableSearchFlow
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                flowOf(emptyList())
            } else {
                weatherRepository.searchLocation(query).onEach {
                    Log.d(TAG, " returned: ///////$it")
                }
                    .catch { e ->
                        Log.e(TAG, "Search error: ${e.message}")
                        emit(emptyList())
                    }
            }

        }.stateIn(scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList())


    override fun deleteFavoriteLocation(storedLocation: StoredLocation) {
        viewModelScope.launch {
           val result = weatherRepository.deleteFavoriteLocation(storedLocation)
            Log.i(TAG, "deleteFavoriteLocation: $result")
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

    override suspend fun searchLocation(searchQuery: String) {
        Log.i(TAG, "searchLocation: //////")
        weatherRepository.searchLocation(searchQuery).collect { locations ->
            Log.i(TAG, "searchLocation: $locations")
        }
    }
}

class FavoriteViwModelFactory(
    private val repo: WeatherRepository, private val location: Location
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteViewModelImp(repo, location) as T
    }
}