package com.playertwo1.rin

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.playertwo1.rin.core.database.RinDatabase
import com.playertwo1.rin.core.database.repository.RoomProjectRepository
import com.playertwo1.rin.core.model.DataOrigin
import com.playertwo1.rin.core.model.LocalCheckpoint
import com.playertwo1.rin.core.model.LocalProject
import com.playertwo1.rin.core.model.ProjectPriority
import com.playertwo1.rin.core.model.ProjectStatus
import com.playertwo1.rin.core.model.SyncState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class CheckpointHistoryCrudTest {

    private lateinit var database: RinDatabase
    private lateinit var repository: RoomProjectRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = RinDatabase.createInMemory(context)
        repository = RoomProjectRepository(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    private suspend fun createTestProject(id: String = UUID.randomUUID().toString()): LocalProject {
        val project = LocalProject(
            id = id,
            origin = DataOrigin.LOCAL,
            name = "Projeto para Checkpoint",
            description = "Descrição de teste",
            priority = ProjectPriority.NORMAL,
            businessStatus = ProjectStatus.ACTIVE,
            syncState = SyncState.LOCAL_ONLY,
            createdAt = 1000L,
            updatedAt = 1000L
        )
        repository.saveProject(project)
        return project
    }

    @Test
    fun createCheckpoint_persistsCorrectlyInRoom() {
        runBlocking {
            val project = createTestProject()
            val checkpointId = UUID.randomUUID().toString()
            val createdAt = 2000L

            val checkpoint = LocalCheckpoint(
                id = checkpointId,
                projectId = project.id,
                title = "Checkpoint 1 - Setup Concluído",
                summary = "Configuração do banco de dados Room finalizada",
                nextStep = "Implementar telas de UI",
                blockers = "Nenhum no momento",
                referencesText = "RinDatabase.kt, CheckpointDao.kt",
                origin = DataOrigin.LOCAL,
                createdAt = createdAt,
                lastConfirmedAt = null
            )

            repository.saveCheckpoint(checkpoint)

            val retrieved = repository.getCheckpoint(checkpointId)
            assertNotNull(retrieved)
            assertEquals(checkpointId, retrieved!!.id)
            assertEquals(project.id, retrieved.projectId)
            assertEquals("Checkpoint 1 - Setup Concluído", retrieved.title)
            assertEquals("Configuração do banco de dados Room finalizada", retrieved.summary)
            assertEquals("Implementar telas de UI", retrieved.nextStep)
            assertEquals("Nenhum no momento", retrieved.blockers)
            assertEquals("RinDatabase.kt, CheckpointDao.kt", retrieved.referencesText)
            assertEquals(DataOrigin.LOCAL, retrieved.origin)
            assertEquals(createdAt, retrieved.createdAt)
            assertNull(retrieved.lastConfirmedAt)

            val checkpoints = repository.observeCheckpoints(project.id).first()
            assertEquals(1, checkpoints.size)
            assertEquals(checkpointId, checkpoints[0].id)
        }
    }

    @Test
    fun multipleCheckpoints_orderedByCreatedAtDesc() {
        runBlocking {
            val project = createTestProject()

            val cp1 = LocalCheckpoint(
                id = "cp-1",
                projectId = project.id,
                title = "Primeiro",
                summary = "Fato 1",
                origin = DataOrigin.LOCAL,
                createdAt = 1000L
            )
            val cp2 = LocalCheckpoint(
                id = "cp-2",
                projectId = project.id,
                title = "Segundo",
                summary = "Fato 2",
                origin = DataOrigin.LOCAL,
                createdAt = 3000L
            )
            val cp3 = LocalCheckpoint(
                id = "cp-3",
                projectId = project.id,
                title = "Terceiro",
                summary = "Fato 3",
                origin = DataOrigin.LOCAL,
                createdAt = 2000L
            )

            repository.saveCheckpoint(cp1)
            repository.saveCheckpoint(cp2)
            repository.saveCheckpoint(cp3)

            val list = repository.observeCheckpoints(project.id).first()
            assertEquals(3, list.size)
            assertEquals("cp-2", list[0].id) // 3000L (mais recente)
            assertEquals("cp-3", list[1].id) // 2000L
            assertEquals("cp-1", list[2].id) // 1000L

            val latest = repository.getLatestCheckpoint(project.id)
            assertNotNull(latest)
            assertEquals("cp-2", latest!!.id)
        }
    }

    @Test
    fun editCheckpoint_updatesSummaryAndPreservesCreatedAtAndId() {
        runBlocking {
            val project = createTestProject()
            val original = LocalCheckpoint(
                id = "cp-edit-1",
                projectId = project.id,
                title = "Título Inicial",
                summary = "Resumo preliminar com imprecisão",
                nextStep = "Revisar",
                origin = DataOrigin.LOCAL,
                createdAt = 5000L
            )
            repository.saveCheckpoint(original)

            val edited = original.copy(
                title = "Título Corrigido",
                summary = "Resumo corrigido com fatos reais verificados",
                nextStep = "Próximo passo atualizado"
            )
            repository.saveCheckpoint(edited)

            val retrieved = repository.getCheckpoint("cp-edit-1")
            assertNotNull(retrieved)
            assertEquals("cp-edit-1", retrieved!!.id)
            assertEquals(5000L, retrieved.createdAt) // Preserva timestamp de criação original
            assertEquals("Título Corrigido", retrieved.title)
            assertEquals("Resumo corrigido com fatos reais verificados", retrieved.summary)
            assertEquals("Próximo passo atualizado", retrieved.nextStep)
        }
    }

    @Test
    fun deleteCheckpoint_removesSpecificCheckpoint_andPreservesOthers() {
        runBlocking {
            val project = createTestProject()

            val cp1 = LocalCheckpoint(
                id = "cp-del-1",
                projectId = project.id,
                title = "Manter",
                summary = "Este fica",
                origin = DataOrigin.LOCAL,
                createdAt = 1000L
            )
            val cp2 = LocalCheckpoint(
                id = "cp-del-2",
                projectId = project.id,
                title = "Remover",
                summary = "Este será excluído",
                origin = DataOrigin.LOCAL,
                createdAt = 2000L
            )
            repository.saveCheckpoint(cp1)
            repository.saveCheckpoint(cp2)

            repository.deleteCheckpoint("cp-del-2")

            val list = repository.observeCheckpoints(project.id).first()
            assertEquals(1, list.size)
            assertEquals("cp-del-1", list[0].id)
            assertNull(repository.getCheckpoint("cp-del-2"))
        }
    }

    @Test
    fun cascadeDelete_whenProjectDeleted_allCheckpointsDeleted() {
        runBlocking {
            val project = createTestProject()

            val cp1 = LocalCheckpoint(
                id = "cp-cascade-1",
                projectId = project.id,
                title = "Checkpoint A",
                summary = "Resumo A",
                origin = DataOrigin.LOCAL,
                createdAt = 1000L
            )
            val cp2 = LocalCheckpoint(
                id = "cp-cascade-2",
                projectId = project.id,
                title = "Checkpoint B",
                summary = "Resumo B",
                origin = DataOrigin.LOCAL,
                createdAt = 2000L
            )
            repository.saveCheckpoint(cp1)
            repository.saveCheckpoint(cp2)

            assertEquals(2, repository.observeCheckpoints(project.id).first().size)

            // Deletar o projeto pai
            repository.deleteProject(project.id)

            // Checkpoints devem ser deletados em cascata via chave estrangeira
            val remainingCheckpoints = repository.observeCheckpoints(project.id).first()
            assertTrue(remainingCheckpoints.isEmpty())
            assertNull(repository.getCheckpoint("cp-cascade-1"))
            assertNull(repository.getCheckpoint("cp-cascade-2"))
        }
    }
}
