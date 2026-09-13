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
import com.playertwo1.rin.ui.screens.projects.checkpoints.CheckpointFormViewModel
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
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class CheckpointFormViewModelTest {

    private class TestProjectRepository : ProjectRepository {
        val projectsMap = mutableMapOf<String, LocalProject>()
        val checkpointsMap = mutableMapOf<String, LocalCheckpoint>()

        override fun observeProjects(): Flow<List<LocalProject>> =
            MutableStateFlow(projectsMap.values.toList())

        override fun observeProject(projectId: String): Flow<LocalProject?> =
            MutableStateFlow(projectsMap[projectId])

        override suspend fun getProject(projectId: String): LocalProject? =
            projectsMap[projectId]

        override suspend fun getProjectByRemoteIdentity(
            workstationId: String,
            remoteProjectId: String
        ): LocalProject? = null

        override suspend fun saveProject(project: LocalProject) {
            projectsMap[project.id] = project
        }

        override suspend fun saveProjects(projects: List<LocalProject>) {
            projects.forEach { projectsMap[it.id] = it }
        }

        override suspend fun deleteProject(projectId: String) {
            projectsMap.remove(projectId)
            checkpointsMap.entries.removeIf { it.value.projectId == projectId }
        }

        override fun observeCheckpoints(projectId: String): Flow<List<LocalCheckpoint>> =
            MutableStateFlow(checkpointsMap.values.filter { it.projectId == projectId }.sortedByDescending { it.createdAt })

        override fun observeLatestCheckpoint(projectId: String): Flow<LocalCheckpoint?> =
            MutableStateFlow(checkpointsMap.values.filter { it.projectId == projectId }.maxByOrNull { it.createdAt })

        override suspend fun getLatestCheckpoint(projectId: String): LocalCheckpoint? =
            checkpointsMap.values.filter { it.projectId == projectId }.maxByOrNull { it.createdAt }

        override suspend fun getCheckpoint(checkpointId: String): LocalCheckpoint? =
            checkpointsMap[checkpointId]

        override suspend fun saveCheckpoint(checkpoint: LocalCheckpoint) {
            checkpointsMap[checkpoint.id] = checkpoint
        }

        override suspend fun saveCheckpoints(checkpoints: List<LocalCheckpoint>) {
            checkpoints.forEach { checkpointsMap[it.id] = it }
        }

        override suspend fun deleteCheckpoint(checkpointId: String) {
            checkpointsMap.remove(checkpointId)
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
    private lateinit var viewModel: CheckpointFormViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = TestProjectRepository()
        viewModel = CheckpointFormViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_isClosed() {
        val state = viewModel.uiState.value
        assertFalse(state.isOpen)
        assertFalse(state.isEditing)
        assertNull(state.checkpointId)
        assertNull(state.summaryError)
    }

    @Test
    fun openForCreate_initializesFormForNewCheckpoint() {
        viewModel.openForCreate("proj-123")
        val state = viewModel.uiState.value
        assertTrue(state.isOpen)
        assertFalse(state.isEditing)
        assertEquals("proj-123", state.projectId)
        assertEquals("Checkpoint", state.title)
        assertEquals("", state.summary)
        assertNull(state.summaryError)
    }

    @Test
    fun openForEdit_populatesFieldsFromExistingCheckpoint() {
        val checkpoint = LocalCheckpoint(
            id = "cp-1",
            projectId = "proj-123",
            title = "Migração Room",
            summary = "Migração de esquema v1 para v2 concluída",
            nextStep = "Adicionar testes unitários",
            blockers = "Nenhum",
            referencesText = "MIGRATION_1_2.kt",
            origin = DataOrigin.LOCAL,
            createdAt = 1000L
        )

        viewModel.openForEdit(checkpoint)
        val state = viewModel.uiState.value
        assertTrue(state.isOpen)
        assertTrue(state.isEditing)
        assertEquals("cp-1", state.checkpointId)
        assertEquals("proj-123", state.projectId)
        assertEquals("Migração Room", state.title)
        assertEquals("Migração de esquema v1 para v2 concluída", state.summary)
        assertEquals("Adicionar testes unitários", state.nextStep)
        assertEquals("Nenhum", state.blockers)
        assertEquals("MIGRATION_1_2.kt", state.referencesText)
    }

    @Test
    fun save_withBlankSummary_setsSummaryErrorAndDoesNotSave() = runTest(testDispatcher) {
        viewModel.openForCreate("proj-123")
        viewModel.updateSummary("   ")
        viewModel.save()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isOpen)
        assertEquals("O resumo factual é obrigatório", state.summaryError)
        assertTrue(repository.checkpointsMap.isEmpty())
    }

    @Test
    fun save_newCheckpoint_persistsInRepositoryWithLocalOrigin() = runTest(testDispatcher) {
        val project = LocalProject(
            id = "proj-123",
            origin = DataOrigin.LOCAL,
            name = "Projeto Teste",
            description = "Desc",
            businessStatus = ProjectStatus.ACTIVE,
            priority = ProjectPriority.HIGH,
            syncState = SyncState.LOCAL_ONLY,
            createdAt = 1000L,
            updatedAt = 1000L
        )
        repository.saveProject(project)

        viewModel.openForCreate("proj-123")
        viewModel.updateTitle("Fase F05 iniciada")
        viewModel.updateSummary("Implementado formulário de checkpoints")
        viewModel.updateNextStep("Criar testes instrumentados")
        viewModel.updateBlockers("Nenhum bloqueio")
        viewModel.updateReferencesText("F05-onde-parei.md")

        var successCalled = false
        viewModel.save(onSuccess = { successCalled = true })
        advanceUntilIdle()

        assertTrue(successCalled)
        assertFalse(viewModel.uiState.value.isOpen)
        assertTrue(viewModel.uiState.value.isSuccess)

        assertEquals(1, repository.checkpointsMap.size)
        val saved = repository.checkpointsMap.values.first()
        assertEquals("proj-123", saved.projectId)
        assertEquals("Fase F05 iniciada", saved.title)
        assertEquals("Implementado formulário de checkpoints", saved.summary)
        assertEquals("Criar testes instrumentados", saved.nextStep)
        assertEquals("Nenhum bloqueio", saved.blockers)
        assertEquals("F05-onde-parei.md", saved.referencesText)
        assertEquals(DataOrigin.LOCAL, saved.origin)
        assertTrue(saved.createdAt > 0L)

        // Atualizou updatedAt do projeto
        val updatedProj = repository.getProject("proj-123")
        assertNotNull(updatedProj)
        assertTrue(updatedProj!!.updatedAt >= saved.createdAt)
    }

    @Test
    fun save_editCheckpoint_preservesIdAndCreatedAt() = runTest(testDispatcher) {
        val project = LocalProject(
            id = "proj-123",
            origin = DataOrigin.LOCAL,
            name = "Projeto Teste",
            description = "Desc",
            createdAt = 500L,
            updatedAt = 500L
        )
        repository.saveProject(project)

        val originalCheckpoint = LocalCheckpoint(
            id = "cp-fixed-id",
            projectId = "proj-123",
            title = "Versão Antiga",
            summary = "Resumo antigo",
            nextStep = "Passo antigo",
            blockers = null,
            referencesText = null,
            origin = DataOrigin.LOCAL,
            createdAt = 12345L
        )
        repository.saveCheckpoint(originalCheckpoint)

        viewModel.openForEdit(originalCheckpoint)
        viewModel.updateTitle("Versão Editada")
        viewModel.updateSummary("Resumo corrigido com fatos detalhados")
        viewModel.updateNextStep("Novo próximo passo")

        viewModel.save()
        advanceUntilIdle()

        val edited = repository.getCheckpoint("cp-fixed-id")
        assertNotNull(edited)
        assertEquals("cp-fixed-id", edited!!.id)
        assertEquals("proj-123", edited.projectId)
        assertEquals(12345L, edited.createdAt) // Preserva timestamp de criação original
        assertEquals("Versão Editada", edited.title)
        assertEquals("Resumo corrigido com fatos detalhados", edited.summary)
        assertEquals("Novo próximo passo", edited.nextStep)
        assertEquals(DataOrigin.LOCAL, edited.origin)
    }

    @Test
    fun dismiss_closesForm() {
        viewModel.openForCreate("proj-123")
        assertTrue(viewModel.uiState.value.isOpen)
        viewModel.dismiss()
        assertFalse(viewModel.uiState.value.isOpen)
    }
}
