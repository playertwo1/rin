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
import com.playertwo1.rin.core.network.fake.FakeWorkstationGateway
import com.playertwo1.rin.core.sync.WorkstationSyncManager
import com.playertwo1.rin.ui.screens.projects.ProjectsViewModel
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectsViewModelTest {

    private class TestProjectRepository : ProjectRepository {
        val projectsFlow = MutableStateFlow<List<LocalProject>>(emptyList())
        val checkpointsFlow = MutableStateFlow<Map<String, List<LocalCheckpoint>>>(emptyMap())

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
            checkpointsFlow.value = checkpointsFlow.value - projectId
        }

        override fun observeCheckpoints(projectId: String): Flow<List<LocalCheckpoint>> =
            checkpointsFlow.map { it[projectId] ?: emptyList() }

        override fun observeLatestCheckpoint(projectId: String): Flow<LocalCheckpoint?> =
            checkpointsFlow.map { it[projectId]?.maxByOrNull { ck -> ck.createdAt } }

        override suspend fun getLatestCheckpoint(projectId: String): LocalCheckpoint? =
            checkpointsFlow.value[projectId]?.maxByOrNull { ck -> ck.createdAt }

        override suspend fun getCheckpoint(checkpointId: String): LocalCheckpoint? =
            checkpointsFlow.value.values.flatten().find { it.id == checkpointId }

        override suspend fun saveCheckpoint(checkpoint: LocalCheckpoint) {
            val list = checkpointsFlow.value[checkpoint.projectId] ?: emptyList()
            checkpointsFlow.value = checkpointsFlow.value + (checkpoint.projectId to (list.filterNot { it.id == checkpoint.id } + checkpoint))
        }

        override suspend fun saveCheckpoints(checkpoints: List<LocalCheckpoint>) {}

        override suspend fun deleteCheckpoint(checkpointId: String) {
            checkpointsFlow.value = checkpointsFlow.value.mapValues { (_, list) ->
                list.filterNot { it.id == checkpointId }
            }
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
    private lateinit var syncManager: WorkstationSyncManager
    private lateinit var viewModel: ProjectsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = TestProjectRepository()
        syncManager = WorkstationSyncManager(FakeWorkstationGateway(), repository)
        viewModel = ProjectsViewModel(repository, syncManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun toggleArchive_archivesActiveProject() = runTest(testDispatcher) {
        val project = LocalProject(
            id = "proj-1",
            origin = DataOrigin.LOCAL,
            name = "Projeto Ativo",
            description = "Desc",
            priority = ProjectPriority.NORMAL,
            businessStatus = ProjectStatus.ACTIVE,
            createdAt = 1000L,
            updatedAt = 1000L
        )
        repository.saveProject(project)
        viewModel.selectProject("proj-1")
        advanceUntilIdle()

        viewModel.toggleArchive("proj-1")
        advanceUntilIdle()

        val updated = repository.getProject("proj-1")
        assertNotNull(updated)
        assertEquals(ProjectStatus.ARCHIVED, updated!!.businessStatus)
        assertEquals(ProjectStatus.ARCHIVED, viewModel.uiState.value.selectedProject?.businessStatus)
    }

    @Test
    fun toggleArchive_unarchivesArchivedProject() = runTest(testDispatcher) {
        val project = LocalProject(
            id = "proj-archived",
            origin = DataOrigin.LOCAL,
            name = "Projeto Arquivado",
            description = "Desc",
            priority = ProjectPriority.LOW,
            businessStatus = ProjectStatus.ARCHIVED,
            createdAt = 1000L,
            updatedAt = 1000L
        )
        repository.saveProject(project)
        viewModel.selectProject("proj-archived")
        advanceUntilIdle()

        viewModel.toggleArchive("proj-archived")
        advanceUntilIdle()

        val updated = repository.getProject("proj-archived")
        assertNotNull(updated)
        assertEquals(ProjectStatus.ACTIVE, updated!!.businessStatus)
        assertEquals(ProjectStatus.ACTIVE, viewModel.uiState.value.selectedProject?.businessStatus)
    }

    @Test
    fun deleteProjectLocally_removesFromRepository_andClearsSelection() = runTest(testDispatcher) {
        val project1 = LocalProject(
            id = "proj-del-1",
            origin = DataOrigin.LOCAL,
            name = "Projeto para Excluir",
            description = "Desc",
            createdAt = 1000L,
            updatedAt = 1000L
        )
        val project2 = LocalProject(
            id = "proj-keep-2",
            origin = DataOrigin.LOCAL,
            name = "Projeto para Manter",
            description = "Desc",
            createdAt = 2000L,
            updatedAt = 2000L
        )
        repository.saveProject(project1)
        repository.saveProject(project2)

        viewModel.selectProject("proj-del-1")
        advanceUntilIdle()
        assertNotNull(viewModel.uiState.value.selectedProject)

        viewModel.deleteProjectLocally("proj-del-1")
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.selectedProject)
        assertNull(repository.getProject("proj-del-1"))
        assertNotNull(repository.getProject("proj-keep-2"))
        assertEquals(1, repository.projectsFlow.value.count { it.origin == DataOrigin.LOCAL })
        assertEquals("proj-keep-2", repository.projectsFlow.value.find { it.origin == DataOrigin.LOCAL }?.id)
    }
}
