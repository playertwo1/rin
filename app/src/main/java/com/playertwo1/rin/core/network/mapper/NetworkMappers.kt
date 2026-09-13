package com.playertwo1.rin.core.network.mapper

import com.playertwo1.rin.core.model.ErrorCategory
import com.playertwo1.rin.core.model.ProjectCheckpoint
import com.playertwo1.rin.core.model.ProjectDetail
import com.playertwo1.rin.core.model.ProjectSummary
import com.playertwo1.rin.core.model.WorkstationError
import com.playertwo1.rin.core.model.WorkstationEvent
import com.playertwo1.rin.core.model.WorkstationHealth
import com.playertwo1.rin.core.network.dto.CheckpointDto
import com.playertwo1.rin.core.network.dto.ErrorDto
import com.playertwo1.rin.core.network.dto.EventDto
import com.playertwo1.rin.core.network.dto.HealthDto
import com.playertwo1.rin.core.network.dto.ProjectDetailDto
import com.playertwo1.rin.core.network.dto.ProjectSummaryDto

fun HealthDto.toDomain(): WorkstationHealth = WorkstationHealth(
    status = status,
    version = version,
    workstationName = workstationName,
    capabilities = capabilities.toSet()
)

fun ProjectSummaryDto.toDomain(): ProjectSummary = ProjectSummary(
    id = id,
    name = name,
    description = description,
    currentBranch = currentBranch,
    status = status,
    updatedAt = updatedAt
)

fun CheckpointDto.toDomain(): ProjectCheckpoint = ProjectCheckpoint(
    id = id,
    title = title,
    summary = summary,
    createdAt = createdAt
)

fun ProjectDetailDto.toDomain(): ProjectDetail = ProjectDetail(
    id = id,
    name = name,
    description = description,
    currentBranch = currentBranch,
    status = status,
    lastCheckpoint = lastCheckpoint?.toDomain(),
    gitCommitHash = gitCommitHash,
    openDecisionsCount = openDecisionsCount,
    updatedAt = updatedAt
)

fun EventDto.toDomain(): WorkstationEvent = WorkstationEvent(
    eventId = eventId,
    cursor = cursor,
    projectId = projectId,
    type = type,
    payloadSummary = payloadSummary,
    occurredAt = occurredAt
)

fun ErrorDto.toDomain(): WorkstationError = WorkstationError(
    code = code,
    message = message,
    category = when (category.lowercase()) {
        "network_unavailable", "network" -> ErrorCategory.NETWORK_UNAVAILABLE
        "timeout" -> ErrorCategory.TIMEOUT
        "not_found" -> ErrorCategory.NOT_FOUND
        "unauthorized" -> ErrorCategory.UNAUTHORIZED
        "forbidden" -> ErrorCategory.FORBIDDEN
        "server_error" -> ErrorCategory.SERVER_ERROR
        "incompatible_version" -> ErrorCategory.INCOMPATIBLE_VERSION
        "capability_missing" -> ErrorCategory.CAPABILITY_MISSING
        else -> ErrorCategory.UNKNOWN
    },
    details = details
)
