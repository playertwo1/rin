package com.playertwo1.rin.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "checkpoints",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["projectId"])
    ]
)
data class CheckpointEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val title: String,
    val summary: String,
    val nextStep: String?,
    val blockers: String?,
    val referencesText: String?,
    val origin: String,
    val createdAt: Long,
    val lastConfirmedAt: Long?
)
