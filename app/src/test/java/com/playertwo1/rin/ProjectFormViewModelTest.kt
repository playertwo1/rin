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
import com.playertwo1.rin.ui.screens.projects.ProjectFormViewModel
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectFormViewModelTest {

    private class TestProjectRepository : ProjectRepository {
        val projectsFlow = MutableStateFlow<List<LocalProject>>(emptyList())

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
            MutableStateFlow(emptyList())

        override fun observeLatestCheckpoint(projectId: String): Flow<LocalCheckpoint?> =
            MutableStateFlow(null)

        override suspend fun getLatestCheckpoint(projectId: String): LocalCheckpoint? = null
        override suspend fun getCheckpoint(checkpointId: String): LocalCheckpoint? = null
        override suspend fun saveCheckpoint(checkpoint: LocalCheckpoint) {}
        override suspend fun saveCheckpoints(checkpoints: List<LocalCheckpoint>) {}
        override suspend fun deleteCheckpoint(checkpointId: String) {}

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
        }
    }

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: TestProjectRepository
    private lateinit var viewModel: ProjectFormViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = TestProjectRepository()
        viewModel = ProjectFormViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_isClosedAndClean() {
        val state = viewModel.uiState.value
        assertFalse(state.isOpen)
        assertFalse(state.isEditing)
        assertNull(state.projectId)
        assertEquals("", state.name)
        assertEquals("", state.description)
        assertEquals(ProjectPriority.NORMAL, state.priority)
        assertEquals(ProjectStatus.ACTIVE, state.status)
        assertNull(state.nameError)
    }

    @Test
    fun openForCreate_initializesDefaults() {
        viewModel.openForCreate()
        val state = viewModel.uiState.value
        assertTrue(state.isOpen)
        assertFalse(state.isEditing)
        assertNull(state.projectId)
        assertEquals("", state.name)
        assertEquals("", state.description)
        assertEquals(ProjectPriority.NORMAL, state.priority)
        assertEquals(ProjectStatus.ACTIVE, state.status)
        assertNull(state.nameError)
    }

    @Test
    fun openForEdit_populatesExistingProjectValues() {
        val project = LocalProject(
            id = "proj-123",
            origin = DataOrigin.LOCAL,
            name = "App Piloto",
            description = "Testando projeto local",
            priority = ProjectPriority.HIGH,
            businessStatus = ProjectStatus.PAUSED,
            createdAt = 1000L,
            updatedAt = 1000L
        )

        viewModel.openForEdit(project)
        val state = viewModel.uiState.value
        assertTrue(state.isOpen)
        assertTrue(state.isEditing)
        assertEquals("proj-123", state.projectId)
        assertEquals("App Piloto", state.name)
        assertEquals("Testando projeto local", state.description)
        assertEquals(ProjectPriority.HIGH, state.priority)
        assertEquals(ProjectStatus.PAUSED, state.status)
        assertNull(state.nameError)
    }

    @Test
    fun save_withBlankName_setsNameError_andDoesNotPersist() = runTest(testDispatcher) {
        viewModel.openForCreate()
        viewModel.updateName("   ")
        viewModel.updateDescription("Sem nome")

        var successCalled = false
        viewModel.save(onSuccess = { successCalled = true })
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isOpen)
        assertFalse(state.isSuccess)
        assertFalse(successCalled)
        assertEquals("O nome do projeto é obrigatório", state.nameError)
        assertTrue(repository.projectsFlow.value.isEmpty())
    }

    @Test
    fun save_newLocalProject_persistsWithCorrectInvariants() = runTest(testDispatcher) {
        viewModel.openForCreate()
        viewModel.updateName("RIN Mobile Client")
        viewModel.updateDescription("Cliente Android nativo com Jetpack Compose")
        viewModel.updatePriority(ProjectPriority.URGENT)
        viewModel.updateStatus(ProjectStatus.ACTIVE)

        var successCalled = false
        viewModel.save(onSuccess = { successCalled = true })
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isOpen)
        assertTrue(state.isSuccess)
        assertTrue(successCalled)

        val savedList = repository.projectsFlow.value
        assertEquals(1, savedList.size)
        val saved = savedList.first()
        assertNotNull(saved.id)
        assertTrue(saved.id.isNotBlank())
        assertEquals("RIN Mobile Client", saved.name)
        assertEquals("Cliente Android nativo com Jetpack Compose", saved.description)
        assertEquals(ProjectPriority.URGENT, saved.priority)
        assertEquals(ProjectStatus.ACTIVE, saved.businessStatus)
        assertEquals(DataOrigin.LOCAL, saved.origin)
        assertEquals(SyncState.LOCAL_ONLY, saved.syncState)
        assertNull(saved.workstationId)
        assertNull(saved.remoteProjectId)
        assertNull(saved.lastConfirmedAt)
        assertTrue(saved.createdAt > 0L)
        assertEquals(saved.createdAt, saved.updatedAt)
    }

    @Test
    fun save_editExistingLocalProject_updatesFields_andPreservesIdAndCreatedAt() = runTest(testDispatcher) {
        val initialProject = LocalProject(
            id = "proj-existing-1",
            origin = DataOrigin.LOCAL,
            name = "Nome Original",
            description = "Desc Original",
            priority = ProjectPriority.LOW,
            businessStatus = ProjectStatus.ACTIVE,
            createdAt = 1000L,
            updatedAt = 1000L
        )
        repository.saveProject(initialProject)

        viewModel.openForEdit(initialProject)
        viewModel.updateName("Nome Atualizado")
        viewModel.updateDescription("Desc Atualizada")
        viewModel.updatePriority(ProjectPriority.HIGH)
        viewModel.updateStatus(ProjectStatus.PAUSED)

        var successCalled = false
        viewModel.save(onSuccess = { successCalled = true })
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isOpen)
        assertTrue(state.isSuccess)
        assertTrue(successCalled)

        val updated = repository.getProject("proj-existing-1")
        assertNotNull(updated)
        assertEquals("proj-existing-1", updated!!.id)
        assertEquals("Nome Atualizado", updated.name)
        assertEquals("Desc Atualizada", updated.description)
        assertEquals(ProjectPriority.HIGH, updated.priority)
        assertEquals(ProjectStatus.PAUSED, updated.businessStatus)
        assertEquals(1000L, updated.createdAt)
        assertTrue(updated.updatedAt >= 1000L)
        assertEquals(DataOrigin.LOCAL, updated.origin)
        assertEquals(SyncState.LOCAL_ONLY, updated.syncState)
    }

    @Test
    fun dismiss_closesFormWithoutPersisting() = runTest(testDispatcher) {
        viewModel.openForCreate()
        viewModel.updateName("Projeto Cancelado")
        viewModel.updateDescription("Não deve ser salvo")

        viewModel.dismiss()
        val state = viewModel.uiState.value
        assertFalse(state.isOpen)
        assertTrue(repository.projectsFlow.value.isEmpty())
    }
}
