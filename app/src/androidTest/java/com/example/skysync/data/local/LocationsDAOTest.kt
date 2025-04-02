package com.example.skysync.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.skysync.models.StoredLocation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class LocationsDAOTest {
    private lateinit var dao: LocationsDAO
    private lateinit var dataBase: DataBase


    @Before
    fun setUp() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), DataBase::class.java
        ).build()
        dao = dataBase.getLocationDao()
    }

    @Test
    fun insertLocation_getAllLocations_returnsInsertedLocation() = runTest {
        //Given
        val location = StoredLocation(lat = 12.344444, lon = 34.23133, name = "cairo")
        val location2 = StoredLocation(id = 11, lat = 70.344444, lon = 3.23133, name = "london")

        dao.insertLocation(location)
        dao.insertLocation(location2)
        //When
        val result = dao.getAllLocations().first()[0]
        val result2 = dao.getAllLocations().first()[1]

        //Then
        assertNotNull(result)
        assertNotNull(result2)

        assertThat(result.name, `is`(location.name))
        assertThat(result.lat, `is`(location.lat))
        assertThat(result.lon, `is`(location.lon))
        ///
        assertThat(result2, `is`(location2))
    }

    @Test
    fun deleteLocation_locationIsRemoved() = runTest {
        //Given
        val location = StoredLocation(id = 20, lat = 12.344444, lon = 34.23133, name = "cairo")
        dao.insertLocation(location)
        //When
        dao.deleteLocation(location)
        val result = dao.getAllLocations().first()
        //Then
        assertThat(result , `is`(emptyList<StoredLocation>()))

    }

    @After
    fun tearDown() {
        dataBase.close()
    }
}