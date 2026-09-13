package com.playertwo1.rin.core.model

data class WorkstationHealth(
    val status: String,
    val version: String,
    val workstationName: String,
    val capabilities: Set<String>
) {
    val isHealthy: Boolean get() = status.equals("healthy", ignoreCase = true)
    fun hasCapability(capability: String): Boolean = capabilities.contains(capability)
}

data class ProjectSummary(
    val id: String,
    val name: String,
    val description: String,
    val currentBranch: String,
    val status: String,
    val updatedAt: String
)

data class ProjectCheckpoint(
    val id: String,
    val title: String,
    val summary: String,
    val createdAt: String
)

data class ProjectDetail(
    val id: String,
    val name: String,
    val description: String,
    val currentBranch: String,
    val status: String,
    val lastCheckpoint: ProjectCheckpoint?,
    val gitCommitHash: String,
    val openDecisionsCount: Int,
    val updatedAt: String
)

data class WorkstationEvent(
    val eventId: String,
    val cursor: String,
    val projectId: String,
    val type: String,
    val payloadSummary: String,
    val occurredAt: String
)
