package com.example.skysync.repo

import android.util.Log
import com.example.skysync.data.local.FakeLocalDataSource
import com.example.skysync.data.remote.FakeRemoteDateSource
import com.example.skysync.models.StoredLocation
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class WeatherRepositoryImpTest {
    private lateinit var fakeRemoteDateSource: FakeRemoteDateSource
    private lateinit var fakeLocalDataSource: FakeLocalDataSource
    private lateinit var repository: WeatherRepositoryImp

    @Before
    fun setUp() {

        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        fakeLocalDataSource = FakeLocalDataSource()
        fakeRemoteDateSource = FakeRemoteDateSource()
        repository = WeatherRepositoryImp(fakeRemoteDateSource, fakeLocalDataSource)
    }


    @Test
    fun getFavoriteLocations_returnsStoredLocations() = runBlocking {
        // Given
        val storedLocation =
            StoredLocation(id = 1, name = "Giza", lat = 29.3845479, lon = 30.458794)

        repository.adNewFavoriteLocations(storedLocation)

        // When
        val result = repository.getFavoriteLocations().first()

        // Then
        assertThat(result.size, `is`(1))
        assertThat(result[0], `is`(storedLocation))
    }

    @Test
    fun deleteFavoriteLocation_removesLocation() = runBlocking {
        // Given
        val storedLocation =
            StoredLocation(id = 1, name = "Giza", lat = 29.3845479, lon = 30.458794)
        repository.adNewFavoriteLocations(storedLocation)

        // When
        repository.deleteFavoriteLocation(storedLocation)
        val result = repository.getFavoriteLocations().first()

        // Then
        assertThat(result.size, `is`(0))
    }
    @Test
    fun getCurrentWeather_returnsExpectedWeatherData() = runBlocking {
        // Given
        val expectedWeather = fakeRemoteDateSource.currentWeatherResponse
        // When
        val result = repository.getCurrentWeather(0.0, 0.0, "en", "metric").first()
        // Then
        assertThat(result, `is`(expectedWeather))
    }

}