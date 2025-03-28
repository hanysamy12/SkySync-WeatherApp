package com.example.skysync.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "locations")
data class StoredLocation(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    val name: String? = null
)
