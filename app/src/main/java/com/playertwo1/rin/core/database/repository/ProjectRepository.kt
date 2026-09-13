package com.playertwo1.rin.core.database.repository

import com.playertwo1.rin.core.model.LocalCheckpoint
import com.playertwo1.rin.core.model.LocalDecisionDraft
import com.playertwo1.rin.core.model.LocalProject
import com.playertwo1.rin.core.model.WorkstationMetadata
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun observeProjects(): Flow<List<LocalProject>>
    fun observeProject(projectId: String): Flow<LocalProject?>
    suspend fun getProject(projectId: String): LocalProject?
    suspend fun getProjectByRemoteIdentity(workstationId: String, remoteProjectId: String): LocalProject?
    suspend fun saveProject(project: LocalProject)
    suspend fun saveProjects(projects: List<LocalProject>)
    suspend fun deleteProject(projectId: String)

    fun observeCheckpoints(projectId: String): Flow<List<LocalCheckpoint>>
    fun observeLatestCheckpoint(projectId: String): Flow<LocalCheckpoint?>
    suspend fun getLatestCheckpoint(projectId: String): LocalCheckpoint?
    suspend fun getCheckpoint(checkpointId: String): LocalCheckpoint?
    suspend fun saveCheckpoint(checkpoint: LocalCheckpoint)
    suspend fun saveCheckpoints(checkpoints: List<LocalCheckpoint>)
    suspend fun deleteCheckpoint(checkpointId: String)

    fun observeDecisions(projectId: String): Flow<List<LocalDecisionDraft>>
    suspend fun saveDecision(decision: LocalDecisionDraft)
    suspend fun deleteDecision(decisionId: String)

    fun observeWorkstationCache(workstationId: String): Flow<WorkstationMetadata?>
    suspend fun getWorkstationCache(workstationId: String): WorkstationMetadata?
    suspend fun saveWorkstationCache(metadata: WorkstationMetadata)

    suspend fun saveProjectWithCheckpointAtomic(project: LocalProject, checkpoint: LocalCheckpoint)
}
