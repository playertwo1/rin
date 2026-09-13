package com.playertwo1.rin.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "projects",
    indices = [
        Index(value = ["workstationId", "remoteProjectId"])
    ]
)
data class ProjectEntity(
    @PrimaryKey val id: String,
    val workstationId: String?,
    val remoteProjectId: String?,
    val origin: String,
    val name: String,
    val description: String,
    val currentBranch: String?,
    val gitCommitHash: String?,
    val businessStatus: String,
    val priority: String = "NORMAL",
    val syncState: String,
    val quotaUsagePercent: Float?,
    val testRunStatus: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val lastConfirmedAt: Long?
)
