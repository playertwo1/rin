package com.playertwo1.rin.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workstation_cache")
data class WorkstationCacheEntity(
    @PrimaryKey val workstationId: String,
    val workstationName: String,
    val version: String,
    val status: String,
    val capabilitiesJson: String,
    val lastConfirmedAt: Long
)
