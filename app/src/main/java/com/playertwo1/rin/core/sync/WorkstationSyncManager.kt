package com.playertwo1.rin.core.sync

import com.playertwo1.rin.core.database.repository.ProjectRepository
import com.playertwo1.rin.core.model.DataOrigin
import com.playertwo1.rin.core.model.LocalCheckpoint
import com.playertwo1.rin.core.model.LocalProject
import com.playertwo1.rin.core.model.NetworkResult
import com.playertwo1.rin.core.model.ProjectStatus
import com.playertwo1.rin.core.model.SyncState
import com.playertwo1.rin.core.model.WorkstationMetadata
import com.playertwo1.rin.core.network.WorkstationGateway
import java.util.UUID

class WorkstationSyncManager(
    private val gateway: WorkstationGateway,
    private val repository: ProjectRepository
) {

    suspend fun syncProjects(workstationId: String = "workstation-lan-default"): Result<Unit> {
        val now = System.currentTimeMillis()

        // 1. Tentar sincronizar saúde e capacidades da Workstation
        when (val healthResult = gateway.getHealth()) {
            is NetworkResult.Success -> {
                val health = healthResult.data
                repository.saveWorkstationCache(
                    WorkstationMetadata(
                        workstationId = workstationId,
                        workstationName = health.workstationName,
                        version = health.version,
                        status = health.status,
                        capabilities = health.capabilities,
                        lastConfirmedAt = now
                    )
                )
            }
            is NetworkResult.Error -> {
                // Não falha imediatamente se saúde falhar, mas loga/registra
            }
        }

        // 2. Buscar projetos do Gateway
        return when (val projectsResult = gateway.getProjects()) {
            is NetworkResult.Success -> {
                val remoteSummaries = projectsResult.data
                for (summary in remoteSummaries) {
                    val existing = repository.getProjectByRemoteIdentity(workstationId, summary.id)
                    val projectToSave = if (existing != null) {
                        existing.copy(
                            name = summary.name,
                            description = summary.description,
                            currentBranch = summary.currentBranch,
                            businessStatus = parseStatus(summary.status),
                            syncState = SyncState.SYNCED,
                            updatedAt = now,
                            lastConfirmedAt = now
                        )
                    } else {
                        LocalProject(
                            id = UUID.randomUUID().toString(),
                            workstationId = workstationId,
                            remoteProjectId = summary.id,
                            origin = DataOrigin.WORKSTATION_REMOTE,
                            name = summary.name,
                            description = summary.description,
                            currentBranch = summary.currentBranch,
                            gitCommitHash = null,
                            businessStatus = parseStatus(summary.status),
                            syncState = SyncState.SYNCED,
                            quotaUsagePercent = null,
                            testRunStatus = null,
                            createdAt = now,
                            updatedAt = now,
                            lastConfirmedAt = now
                        )
                    }
                    repository.saveProject(projectToSave)
                }
                Result.success(Unit)
            }
            is NetworkResult.Error -> {
                // Invariante de resiliência: NÃO apagar dados locais quando a rede/fake falhar!
                Result.failure(Exception("${projectsResult.error.code}: ${projectsResult.error.message}"))
            }
        }
    }

    suspend fun syncProjectDetail(localProjectId: String): Result<Unit> {
        val project = repository.getProject(localProjectId)
            ?: return Result.failure(IllegalArgumentException("Projeto local não encontrado: $localProjectId"))

        if (project.origin == DataOrigin.LOCAL || project.remoteProjectId == null) {
            // Projeto puramente local não possui representação remota a consultar
            return Result.success(Unit)
        }

        val now = System.currentTimeMillis()
        return when (val detailResult = gateway.getProjectDetail(project.remoteProjectId)) {
            is NetworkResult.Success -> {
                val detail = detailResult.data
                val updatedProject = project.copy(
                    name = detail.name,
                    description = detail.description,
                    currentBranch = detail.currentBranch,
                    gitCommitHash = detail.gitCommitHash,
                    businessStatus = parseStatus(detail.status),
                    syncState = SyncState.SYNCED,
                    updatedAt = now,
                    lastConfirmedAt = now
                )
                repository.saveProject(updatedProject)

                if (detail.lastCheckpoint != null) {
                    val ck = detail.lastCheckpoint
                    val checkpointToSave = LocalCheckpoint(
                        id = ck.id.ifBlank { UUID.randomUUID().toString() },
                        projectId = project.id,
                        title = ck.title,
                        summary = ck.summary,
                        origin = DataOrigin.WORKSTATION_REMOTE,
                        createdAt = now,
                        lastConfirmedAt = now
                    )
                    repository.saveCheckpoint(checkpointToSave)
                }
                Result.success(Unit)
            }
            is NetworkResult.Error -> {
                // Preservar dados e checkpoints locais
                Result.failure(Exception("${detailResult.error.code}: ${detailResult.error.message}"))
            }
        }
    }

    private fun parseStatus(status: String): ProjectStatus {
        return when (status.uppercase()) {
            "ACTIVE", "ATIVO" -> ProjectStatus.ACTIVE
            "PAUSED", "PAUSADO" -> ProjectStatus.PAUSED
            "COMPLETED", "CONCLUÍDO", "CONCLUIDO" -> ProjectStatus.COMPLETED
            "ARCHIVED", "ARQUIVADO" -> ProjectStatus.ARCHIVED
            else -> ProjectStatus.UNKNOWN
        }
    }
}
