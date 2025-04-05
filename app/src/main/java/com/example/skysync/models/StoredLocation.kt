package com.example.skysync.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "locations")
data class StoredLocation(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    val name: String? = null
)

@Entity(tableName = "alerts")
data class Alert(
    @PrimaryKey
    val id: UUID,
    val time: String?=null,
    val lat: Double? = null,
    val lon: Double? = null,
    var name: String? = null
)