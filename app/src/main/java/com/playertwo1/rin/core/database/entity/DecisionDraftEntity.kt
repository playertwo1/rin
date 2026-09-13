package com.playertwo1.rin.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "decision_drafts",
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
data class DecisionDraftEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val question: String,
    val optionsJson: String,
    val selectedOption: String?,
    val isConfirmedLocally: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)
