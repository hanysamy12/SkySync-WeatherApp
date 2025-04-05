package com.example.skysync.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import com.example.skysync.models.Alert
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface AlertsDAO {
    @Query("SELECT * FROM alerts")
    fun getAllLocations() : Flow<List<Alert>>

    @Insert(onConflict = IGNORE)
    suspend fun insertLocation(alert: Alert): Long

    @Query("DELETE FROM alerts WHERE id = :alertId")
    suspend fun deleteLocation(alertId :UUID) : Int
}