package com.playertwo1.rin

import com.playertwo1.rin.core.database.repository.ProjectRepository
import com.playertwo1.rin.core.model.DataOrigin
import com.playertwo1.rin.core.model.LocalCheckpoint
import com.playertwo1.rin.core.model.LocalDecisionDraft
import com.playertwo1.rin.core.model.LocalProject
import com.playertwo1.rin.core.model.ProjectPriority
import com.playertwo1.rin.core.model.ProjectStatus
import com.playertwo1.rin.core.model.SyncState
import com.playertwo1.rin.core.model.WorkstationMetadata
import com.playertwo1.rin.ui.screens.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private class TestProjectRepository : ProjectRepository {
        val projectsFlow = MutableStateFlow<List<LocalProject>>(emptyList())
        val checkpointsMap = mutableMapOf<String, LocalCheckpoint>()

        override fun observeProjects(): Flow<List<LocalProject>> = projectsFlow
        override fun observeProject(projectId: String): Flow<LocalProject?> =
            projectsFlow.map { list -> list.find { it.id == projectId } }

        override suspend fun getProject(projectId: String): LocalProject? =
            projectsFlow.value.find { it.id == projectId }

        override suspend fun getProjectByRemoteIdentity(
            workstationId: String,
            remoteProjectId: String
        ): LocalProject? = projectsFlow.value.find {
            it.workstationId == workstationId && it.remoteProjectId == remoteProjectId
        }

        override suspend fun saveProject(project: LocalProject) {
            projectsFlow.value = projectsFlow.value.filterNot { it.id == project.id } + project
        }

        override suspend fun saveProjects(projects: List<LocalProject>) {
            projectsFlow.value = projects
        }

        override suspend fun deleteProject(projectId: String) {
            projectsFlow.value = projectsFlow.value.filterNot { it.id == projectId }
        }

        override fun observeCheckpoints(projectId: String): Flow<List<LocalCheckpoint>> =
            MutableStateFlow(checkpointsMap.values.filter { it.projectId == projectId })

        override fun observeLatestCheckpoint(projectId: String): Flow<LocalCheckpoint?> =
            MutableStateFlow(checkpointsMap[projectId])

        override suspend fun getLatestCheckpoint(projectId: String): LocalCheckpoint? =
            checkpointsMap[projectId]

        override suspend fun getCheckpoint(checkpointId: String): LocalCheckpoint? =
            checkpointsMap.values.find { it.id == checkpointId }

        override suspend fun saveCheckpoint(checkpoint: LocalCheckpoint) {
            checkpointsMap[checkpoint.projectId] = checkpoint
        }

        override suspend fun saveCheckpoints(checkpoints: List<LocalCheckpoint>) {
            checkpoints.forEach { checkpointsMap[it.projectId] = it }
        }

        override suspend fun deleteCheckpoint(checkpointId: String) {
            checkpointsMap.entries.removeIf { it.value.id == checkpointId }
        }

        override fun observeDecisions(projectId: String): Flow<List<LocalDecisionDraft>> =
            MutableStateFlow(emptyList())

        override suspend fun saveDecision(decision: LocalDecisionDraft) {}
        override suspend fun deleteDecision(decisionId: String) {}
        override fun observeWorkstationCache(workstationId: String): Flow<WorkstationMetadata?> =
            MutableStateFlow(null)

        override suspend fun getWorkstationCache(workstationId: String): WorkstationMetadata? = null
        override suspend fun saveWorkstationCache(metadata: WorkstationMetadata) {}
        override suspend fun saveProjectWithCheckpointAtomic(
            project: LocalProject,
            checkpoint: LocalCheckpoint
        ) {
            saveProject(project)
            saveCheckpoint(checkpoint)
        }
    }

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: TestProjectRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = TestProjectRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun emptyState_rendersCorrectly_whenNoProjectsExist() = runTest(testDispatcher) {
        val viewModel = HomeViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(0, state.localProjectsCount)
        assertEquals(0, state.cachedProjectsCount)
        assertEquals(0, state.recentProjects.size)
        assertNull("Projeto ativo deve ser nulo quando vazio", state.activeProjectItem)
    }

    @Test
    fun singleProject_withCheckpoint_and_metricsCounted() = runTest(testDispatcher) {
        val localProj = LocalProject(
            id = "proj-local-01",
            origin = DataOrigin.LOCAL,
            name = "App de Notas",
            description = "Notas locais",
            priority = ProjectPriority.HIGH,
            createdAt = 1000L,
            updatedAt = 1000L
        )
        val checkpoint = LocalCheckpoint(
            id = "ck-01",
            projectId = "proj-local-01",
            title = "Arquitetura Pronta",
            summary = "Criado o módulo de dados",
            nextStep = "Construir UI",
            blockers = null,
            createdAt = 1000L
        )

        repository.saveProject(localProj)
        repository.saveCheckpoint(checkpoint)

        val viewModel = HomeViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.localProjectsCount)
        assertEquals(0, state.cachedProjectsCount)
        assertEquals(1, state.recentProjects.size)

        val active = state.activeProjectItem
        assertNotNull(active)
        assertEquals("App de Notas", active?.project?.name)
        assertEquals(ProjectPriority.HIGH, active?.project?.priority)
        assertEquals("Arquitetura Pronta", active?.latestCheckpoint?.title)
        assertEquals("Construir UI", active?.latestCheckpoint?.nextStep)
        assertNull("Bloqueio ausente permanece null (não inventa texto)", active?.latestCheckpoint?.blockers)
    }

    @Test
    fun multipleProjects_orderedDeterministically_andDistinguishesOrigin() = runTest(testDispatcher) {
        val p1 = LocalProject(
            id = "p-old",
            origin = DataOrigin.LOCAL,
            name = "Projeto Antigo",
            description = "Desc",
            createdAt = 1000L,
            updatedAt = 1000L
        )
        val p2 = LocalProject(
            id = "p-remote",
            workstationId = "ws-1",
            remoteProjectId = "remote-1",
            origin = DataOrigin.WORKSTATION_REMOTE,
            name = "Projeto Remoto",
            description = "Da Workstation",
            createdAt = 2000L,
            updatedAt = 2000L
        )

        repository.saveProjects(listOf(p2, p1))

        val viewModel = HomeViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.localProjectsCount)
        assertEquals(1, state.cachedProjectsCount)
        assertEquals(2, state.recentProjects.size)

        // O ativo deve ser o mais recente (p2)
        assertEquals("p-remote", state.activeProjectItem?.project?.id)
        assertEquals(DataOrigin.WORKSTATION_REMOTE, state.activeProjectItem?.project?.origin)
    }
}
