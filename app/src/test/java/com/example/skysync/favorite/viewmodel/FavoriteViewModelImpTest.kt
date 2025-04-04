package com.example.skysync.favorite.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.skysync.data.Location
import com.example.skysync.helper.Response
import com.example.skysync.models.StoredLocation
import com.example.skysync.repo.WeatherRepository
import com.google.android.gms.maps.model.LatLng
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteViewModelImpTest {
    lateinit var repo: WeatherRepository
    lateinit var viewModel : FavoriteViewModelImp
    lateinit var location: Location

    @Before
    fun setUp() {
        repo = mockk(relaxed = true)
        location = mockk(relaxed = true)
        viewModel = FavoriteViewModelImp(repo,location)
    }
    @Test
    fun addFavoriteLocation_call_repo_with_correct_data()= runTest{
        //Given
        val latLon = LatLng (29.633,30.422)
        val name = "Egypt, Giza"
        val returnedLocation = StoredLocation(
            lat = latLon.latitude,
            lon = latLon.longitude,
            name = name
        )
        coEvery { location.getGeoLocation(latLon.latitude,latLon.longitude) } returns name
        //Then
        val result = viewModel.addFavoriteLocation(latLon)
        assertThat(result,notNullValue())
        coVerify (exactly = 1){ repo.adNewFavoriteLocations(returnedLocation) }
    }
    @Test
    fun getAllFavoriteLocations_success_when_repository_succeeds()=runTest {
            //Given
        val testLocations = listOf(
            StoredLocation(1,30.0, 31.0, "Cairo"),
            StoredLocation(2,40.0, -74.0, "Giza")
        )
        coEvery { repo.getFavoriteLocations() } returns flowOf(testLocations)
        //When
        val result = viewModel.favoriteList.value
        assertThat(result,`is`(Response.Loading))
    }
}