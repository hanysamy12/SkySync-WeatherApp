package com.example.skysync.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.skysync.models.StoredLocation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class WeatherLocalDataSourceImpTest {

    private lateinit var dao: LocationsDAO
    private lateinit var dataBase: DataBase
    private lateinit var localDataSource: WeatherLocalDataSource

    @Before
    fun setUp() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), DataBase::class.java
        ).allowMainThreadQueries().build()
        dao = dataBase.getLocationDao()
        localDataSource =
            WeatherLocalDataSourceImp.getInstance(ApplicationProvider.getApplicationContext()) as WeatherLocalDataSourceImp
    }

    @Test
    fun insertLocation_getAllLocations_returnsInsertedLocation() = runTest {
        //Given
        val location = StoredLocation(id = 20, lat = 12.344444, lon = 34.23133, name = "cairo")
        localDataSource.insertLocation(location)
        //When
        val locations = localDataSource.getAllLocations().first()
        //Then
        assertThat(listOf(location), `is`(locations))
    }

    @Test
    fun deleteLocation_locationIsRemoved() = runTest {
        //Given
        val location = StoredLocation(id = 20, lat = 12.344444, lon = 34.23133, name = "cairo")
        localDataSource.insertLocation(location)
        localDataSource.deleteLocation(location)
        //When
        val locations = localDataSource.getAllLocations().first()
        //Then
        assertThat(emptyList<StoredLocation>(), `is`(locations))
    }

    @After
    fun tearDown() {
        dataBase.close()
    }
}