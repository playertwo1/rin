package com.playertwo1.rin.core.database.mapper

import com.playertwo1.rin.core.database.entity.CheckpointEntity
import com.playertwo1.rin.core.database.entity.DecisionDraftEntity
import com.playertwo1.rin.core.database.entity.ProjectEntity
import com.playertwo1.rin.core.database.entity.WorkstationCacheEntity
import com.playertwo1.rin.core.model.DataOrigin
import com.playertwo1.rin.core.model.LocalCheckpoint
import com.playertwo1.rin.core.model.LocalDecisionDraft
import com.playertwo1.rin.core.model.LocalProject
import com.playertwo1.rin.core.model.ProjectStatus
import com.playertwo1.rin.core.model.SyncState
import com.playertwo1.rin.core.model.WorkstationMetadata
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import com.playertwo1.rin.core.model.ProjectPriority

private val json = Json { ignoreUnknownKeys = true }

fun ProjectEntity.toDomain(): LocalProject = LocalProject(
    id = id,
    workstationId = workstationId,
    remoteProjectId = remoteProjectId,
    origin = try { DataOrigin.valueOf(origin) } catch (e: Exception) { DataOrigin.LOCAL },
    name = name,
    description = description,
    currentBranch = currentBranch,
    gitCommitHash = gitCommitHash,
    businessStatus = try { ProjectStatus.valueOf(businessStatus) } catch (e: Exception) { ProjectStatus.ACTIVE },
    priority = try { ProjectPriority.valueOf(priority) } catch (e: Exception) { ProjectPriority.NORMAL },
    syncState = try { SyncState.valueOf(syncState) } catch (e: Exception) { SyncState.LOCAL_ONLY },
    quotaUsagePercent = quotaUsagePercent,
    testRunStatus = testRunStatus,
    createdAt = createdAt,
    updatedAt = updatedAt,
    lastConfirmedAt = lastConfirmedAt
)

fun LocalProject.toEntity(): ProjectEntity = ProjectEntity(
    id = id,
    workstationId = workstationId,
    remoteProjectId = remoteProjectId,
    origin = origin.name,
    name = name,
    description = description,
    currentBranch = currentBranch,
    gitCommitHash = gitCommitHash,
    businessStatus = businessStatus.name,
    priority = priority.name,
    syncState = syncState.name,
    quotaUsagePercent = quotaUsagePercent,
    testRunStatus = testRunStatus,
    createdAt = createdAt,
    updatedAt = updatedAt,
    lastConfirmedAt = lastConfirmedAt
)

fun CheckpointEntity.toDomain(): LocalCheckpoint = LocalCheckpoint(
    id = id,
    projectId = projectId,
    title = title,
    summary = summary,
    nextStep = nextStep,
    blockers = blockers,
    referencesText = referencesText,
    origin = try { DataOrigin.valueOf(origin) } catch (e: Exception) { DataOrigin.LOCAL },
    createdAt = createdAt,
    lastConfirmedAt = lastConfirmedAt
)

fun LocalCheckpoint.toEntity(): CheckpointEntity = CheckpointEntity(
    id = id,
    projectId = projectId,
    title = title,
    summary = summary,
    nextStep = nextStep,
    blockers = blockers,
    referencesText = referencesText,
    origin = origin.name,
    createdAt = createdAt,
    lastConfirmedAt = lastConfirmedAt
)

fun DecisionDraftEntity.toDomain(): LocalDecisionDraft {
    val optionsList: List<String> = try {
        json.decodeFromString(optionsJson)
    } catch (e: Exception) {
        emptyList()
    }
    return LocalDecisionDraft(
        id = id,
        projectId = projectId,
        question = question,
        options = optionsList,
        selectedOption = selectedOption,
        isConfirmedLocally = isConfirmedLocally,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun LocalDecisionDraft.toEntity(): DecisionDraftEntity = DecisionDraftEntity(
    id = id,
    projectId = projectId,
    question = question,
    optionsJson = json.encodeToString(options),
    selectedOption = selectedOption,
    isConfirmedLocally = isConfirmedLocally,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun WorkstationCacheEntity.toDomain(): WorkstationMetadata {
    val caps: Set<String> = try {
        json.decodeFromString(capabilitiesJson)
    } catch (e: Exception) {
        emptySet()
    }
    return WorkstationMetadata(
        workstationId = workstationId,
        workstationName = workstationName,
        version = version,
        status = status,
        capabilities = caps,
        lastConfirmedAt = lastConfirmedAt
    )
}

fun WorkstationMetadata.toEntity(): WorkstationCacheEntity = WorkstationCacheEntity(
    workstationId = workstationId,
    workstationName = workstationName,
    version = version,
    status = status,
    capabilitiesJson = json.encodeToString(capabilities),
    lastConfirmedAt = lastConfirmedAt
)
