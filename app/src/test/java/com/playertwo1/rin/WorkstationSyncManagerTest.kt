package com.playertwo1.rin

import com.playertwo1.rin.core.database.repository.ProjectRepository
import com.playertwo1.rin.core.model.DataOrigin
import com.playertwo1.rin.core.model.LocalCheckpoint
import com.playertwo1.rin.core.model.LocalDecisionDraft
import com.playertwo1.rin.core.model.LocalProject
import com.playertwo1.rin.core.model.ProjectStatus
import com.playertwo1.rin.core.model.SyncState
import com.playertwo1.rin.core.model.WorkstationMetadata
import com.playertwo1.rin.core.network.fake.FakeScenario
import com.playertwo1.rin.core.network.fake.FakeWorkstationGateway
import com.playertwo1.rin.core.sync.WorkstationSyncManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.UUID

class WorkstationSyncManagerTest {

    private class InMemoryProjectRepository : ProjectRepository {
        private val projectsFlow = MutableStateFlow<Map<String, LocalProject>>(emptyMap())
        private val checkpointsFlow = MutableStateFlow<Map<String, LocalCheckpoint>>(emptyMap())
        private val decisionsFlow = MutableStateFlow<Map<String, LocalDecisionDraft>>(emptyMap())
        private val workstationCacheFlow = MutableStateFlow<Map<String, WorkstationMetadata>>(emptyMap())

        override fun observeProjects(): Flow<List<LocalProject>> =
            projectsFlow.map { it.values.sortedByDescending { p -> p.updatedAt } }

        override fun observeProject(projectId: String): Flow<LocalProject?> =
            projectsFlow.map { it[projectId] }

        override suspend fun getProject(projectId: String): LocalProject? =
            projectsFlow.value[projectId]

        override suspend fun getProjectByRemoteIdentity(
            workstationId: String,
            remoteProjectId: String
        ): LocalProject? = projectsFlow.value.values.find {
            it.workstationId == workstationId && it.remoteProjectId == remoteProjectId
        }

        override suspend fun saveProject(project: LocalProject) {
            projectsFlow.value = projectsFlow.value + (project.id to project)
        }

        override suspend fun saveProjects(projects: List<LocalProject>) {
            val updated = projectsFlow.value.toMutableMap()
            projects.forEach { updated[it.id] = it }
            projectsFlow.value = updated
        }

        override suspend fun deleteProject(projectId: String) {
            projectsFlow.value = projectsFlow.value - projectId
            checkpointsFlow.value = checkpointsFlow.value.filterValues { it.projectId != projectId }
        }

        override fun observeCheckpoints(projectId: String): Flow<List<LocalCheckpoint>> =
            checkpointsFlow.map { it.values.filter { ck -> ck.projectId == projectId }.sortedByDescending { ck -> ck.createdAt } }

        override fun observeLatestCheckpoint(projectId: String): Flow<LocalCheckpoint?> =
            checkpointsFlow.map {
                it.values.filter { ck -> ck.projectId == projectId }.maxByOrNull { ck -> ck.createdAt }
            }

        override suspend fun getLatestCheckpoint(projectId: String): LocalCheckpoint? =
            checkpointsFlow.value.values.filter { it.projectId == projectId }.maxByOrNull { it.createdAt }

        override suspend fun getCheckpoint(checkpointId: String): LocalCheckpoint? =
            checkpointsFlow.value[checkpointId]

        override suspend fun saveCheckpoint(checkpoint: LocalCheckpoint) {
            checkpointsFlow.value = checkpointsFlow.value + (checkpoint.id to checkpoint)
        }

        override suspend fun saveCheckpoints(checkpoints: List<LocalCheckpoint>) {
            val updated = checkpointsFlow.value.toMutableMap()
            checkpoints.forEach { updated[it.id] = it }
            checkpointsFlow.value = updated
        }

        override suspend fun deleteCheckpoint(checkpointId: String) {
            checkpointsFlow.value = checkpointsFlow.value - checkpointId
        }

        override fun observeDecisions(projectId: String): Flow<List<LocalDecisionDraft>> =
            decisionsFlow.map { it.values.filter { d -> d.projectId == projectId } }

        override suspend fun saveDecision(decision: LocalDecisionDraft) {
            decisionsFlow.value = decisionsFlow.value + (decision.id to decision)
        }

        override suspend fun deleteDecision(decisionId: String) {
            decisionsFlow.value = decisionsFlow.value - decisionId
        }

        override fun observeWorkstationCache(workstationId: String): Flow<WorkstationMetadata?> =
            workstationCacheFlow.map { it[workstationId] }

        override suspend fun getWorkstationCache(workstationId: String): WorkstationMetadata? =
            workstationCacheFlow.value[workstationId]

        override suspend fun saveWorkstationCache(metadata: WorkstationMetadata) {
            workstationCacheFlow.value = workstationCacheFlow.value + (metadata.workstationId to metadata)
        }

        override suspend fun saveProjectWithCheckpointAtomic(
            project: LocalProject,
            checkpoint: LocalCheckpoint
        ) {
            saveProject(project)
            saveCheckpoint(checkpoint)
        }
    }

    private lateinit var gateway: FakeWorkstationGateway
    private lateinit var repository: InMemoryProjectRepository
    private lateinit var syncManager: WorkstationSyncManager

    @Before
    fun setUp() {
        gateway = FakeWorkstationGateway(FakeScenario.DEFAULT)
        repository = InMemoryProjectRepository()
        syncManager = WorkstationSyncManager(gateway, repository)
    }

    @Test
    fun sync_stores_remote_projects_and_marks_last_confirmed_at() = runBlocking {
        val result = syncManager.syncProjects("workstation-test-01")
        assertTrue(result.isSuccess)

        val project1 = repository.getProjectByRemoteIdentity("workstation-test-01", "proj-rin-01")
        assertNotNull(project1)
        assertEquals("rin", project1?.name)
        assertEquals(DataOrigin.WORKSTATION_REMOTE, project1?.origin)
        assertEquals(SyncState.SYNCED, project1?.syncState)
        assertNotNull("lastConfirmedAt deve ser gravado após resposta válida", project1?.lastConfirmedAt)
    }

    @Test
    fun sync_failure_preserves_existing_cached_projects() = runBlocking {
        // 1. Inserir um projeto local do usuário e sincronizar inicialmente 1 vez com sucesso
        val localUserProject = LocalProject(
            id = UUID.randomUUID().toString(),
            origin = DataOrigin.LOCAL,
            name = "Projeto Pessoal do Usuário",
            description = "Criado offline no celular",
            createdAt = 1000L,
            updatedAt = 1000L
        )
        repository.saveProject(localUserProject)

        val initialSync = syncManager.syncProjects("workstation-test-01")
        assertTrue(initialSync.isSuccess)

        // 2. Simular indisponibilidade ou falha do servidor / rede offline
        gateway.setScenario(FakeScenario.OFFLINE)
        val offlineSync = syncManager.syncProjects("workstation-test-01")

        // 3. Verificar que a sincronização reporta falha mas NÃO apaga o cache nem os dados locais
        assertFalse(offlineSync.isSuccess)

        val preservedLocal = repository.getProject(localUserProject.id)
        assertNotNull("Projeto local do usuário NÃO deve ser apagado", preservedLocal)
        assertEquals("Projeto Pessoal do Usuário", preservedLocal?.name)

        val cachedRemote = repository.getProjectByRemoteIdentity("workstation-test-01", "proj-rin-01")
        assertNotNull("Cache remoto da workstation deve ser preservado offline", cachedRemote)
        assertEquals("rin", cachedRemote?.name)
    }

    @Test
    fun sync_project_detail_saves_checkpoint_linked_to_local_project() = runBlocking {
        // Sincronizar lista primeiro
        syncManager.syncProjects("workstation-test-01")
        val project = repository.getProjectByRemoteIdentity("workstation-test-01", "proj-rin-01")
        assertNotNull(project)

        // Sincronizar detalhe do projeto
        val detailResult = syncManager.syncProjectDetail(project!!.id)
        assertTrue(detailResult.isSuccess)

        val checkpoint = repository.getLatestCheckpoint(project.id)
        assertNotNull("Checkpoint deve ser persistido vinculado ao projectId local", checkpoint)
        assertEquals(project.id, checkpoint?.projectId)
        assertEquals("Scaffold F01 validado em emulador", checkpoint?.title)
    }
}
