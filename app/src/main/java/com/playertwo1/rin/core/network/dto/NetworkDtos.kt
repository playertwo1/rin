package com.playertwo1.rin.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class EnvelopeDto<T>(
    val schemaVersion: String,
    val requestId: String,
    val occurredAt: String,
    val data: T? = null,
    val error: ErrorDto? = null
)

@Serializable
data class ErrorDto(
    val code: String,
    val category: String,
    val message: String,
    val details: String? = null
)

@Serializable
data class HealthDto(
    val status: String,
    val version: String,
    val workstationName: String,
    val capabilities: List<String> = emptyList()
)

@Serializable
data class ProjectSummaryDto(
    val id: String,
    val name: String,
    val description: String = "",
    val currentBranch: String = "main",
    val status: String = "unknown",
    val updatedAt: String = ""
)

@Serializable
data class CheckpointDto(
    val id: String,
    val title: String,
    val summary: String = "",
    val createdAt: String = ""
)

@Serializable
data class ProjectDetailDto(
    val id: String,
    val name: String,
    val description: String = "",
    val currentBranch: String = "main",
    val status: String = "unknown",
    val lastCheckpoint: CheckpointDto? = null,
    val gitCommitHash: String = "",
    val openDecisionsCount: Int = 0,
    val updatedAt: String = ""
)

@Serializable
data class EventDto(
    val eventId: String,
    val cursor: String,
    val projectId: String,
    val type: String,
    val payloadSummary: String = "",
    val occurredAt: String = ""
)
