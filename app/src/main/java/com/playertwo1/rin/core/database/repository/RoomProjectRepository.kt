package com.playertwo1.rin.core.database.repository

import androidx.room.withTransaction
import com.playertwo1.rin.core.database.RinDatabase
import com.playertwo1.rin.core.database.mapper.toDomain
import com.playertwo1.rin.core.database.mapper.toEntity
import com.playertwo1.rin.core.model.LocalCheckpoint
import com.playertwo1.rin.core.model.LocalDecisionDraft
import com.playertwo1.rin.core.model.LocalProject
import com.playertwo1.rin.core.model.WorkstationMetadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomProjectRepository(
    private val database: RinDatabase
) : ProjectRepository {

    private val projectDao = database.projectDao()
    private val checkpointDao = database.checkpointDao()
    private val decisionDraftDao = database.decisionDraftDao()
    private val workstationCacheDao = database.workstationCacheDao()

    override fun observeProjects(): Flow<List<LocalProject>> {
        return projectDao.observeAllProjects().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeProject(projectId: String): Flow<LocalProject?> {
        return projectDao.observeProjectById(projectId).map { it?.toDomain() }
    }

    override suspend fun getProject(projectId: String): LocalProject? {
        return projectDao.getProjectById(projectId)?.toDomain()
    }

    override suspend fun getProjectByRemoteIdentity(
        workstationId: String,
        remoteProjectId: String
    ): LocalProject? {
        return projectDao.getByRemoteIdentity(workstationId, remoteProjectId)?.toDomain()
    }

    override suspend fun saveProject(project: LocalProject) {
        projectDao.upsertProject(project.toEntity())
    }

    override suspend fun saveProjects(projects: List<LocalProject>) {
        projectDao.upsertProjects(projects.map { it.toEntity() })
    }

    override suspend fun deleteProject(projectId: String) {
        projectDao.deleteProjectById(projectId)
    }

    override fun observeCheckpoints(projectId: String): Flow<List<LocalCheckpoint>> {
        return checkpointDao.observeCheckpointsForProject(projectId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeLatestCheckpoint(projectId: String): Flow<LocalCheckpoint?> {
        return checkpointDao.observeLatestCheckpointForProject(projectId).map { it?.toDomain() }
    }

    override suspend fun getLatestCheckpoint(projectId: String): LocalCheckpoint? {
        return checkpointDao.getLatestCheckpointForProject(projectId)?.toDomain()
    }

    override suspend fun getCheckpoint(checkpointId: String): LocalCheckpoint? {
        return checkpointDao.getCheckpointById(checkpointId)?.toDomain()
    }

    override suspend fun saveCheckpoint(checkpoint: LocalCheckpoint) {
        checkpointDao.upsertCheckpoint(checkpoint.toEntity())
    }

    override suspend fun saveCheckpoints(checkpoints: List<LocalCheckpoint>) {
        checkpointDao.upsertCheckpoints(checkpoints.map { it.toEntity() })
    }

    override suspend fun deleteCheckpoint(checkpointId: String) {
        checkpointDao.deleteCheckpointById(checkpointId)
    }

    override fun observeDecisions(projectId: String): Flow<List<LocalDecisionDraft>> {
        return decisionDraftDao.observeDecisionsForProject(projectId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveDecision(decision: LocalDecisionDraft) {
        decisionDraftDao.upsertDecisionDraft(decision.toEntity())
    }

    override suspend fun deleteDecision(decisionId: String) {
        decisionDraftDao.deleteDecisionDraftById(decisionId)
    }

    override fun observeWorkstationCache(workstationId: String): Flow<WorkstationMetadata?> {
        return workstationCacheDao.observeWorkstation(workstationId).map { it?.toDomain() }
    }

    override suspend fun getWorkstationCache(workstationId: String): WorkstationMetadata? {
        return workstationCacheDao.getWorkstation(workstationId)?.toDomain()
    }

    override suspend fun saveWorkstationCache(metadata: WorkstationMetadata) {
        workstationCacheDao.upsertWorkstation(metadata.toEntity())
    }

    override suspend fun saveProjectWithCheckpointAtomic(
        project: LocalProject,
        checkpoint: LocalCheckpoint
    ) {
        database.withTransaction {
            projectDao.upsertProject(project.toEntity())
            checkpointDao.upsertCheckpoint(checkpoint.toEntity())
        }
    }
}
