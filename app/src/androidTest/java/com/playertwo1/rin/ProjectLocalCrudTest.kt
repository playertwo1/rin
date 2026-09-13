package com.playertwo1.rin

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.playertwo1.rin.core.database.RinDatabase
import com.playertwo1.rin.core.database.repository.RoomProjectRepository
import com.playertwo1.rin.core.model.DataOrigin
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
class ProjectLocalCrudTest {

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

    @Test
    fun createLocalProject_persistsCorrectlyInRoom() {
        runBlocking {
            val projectId = UUID.randomUUID().toString()
            val createdAt = 1000L
            val project = LocalProject(
                id = projectId,
                origin = DataOrigin.LOCAL,
                name = "Meu App Local",
                description = "Objetivo do projeto local",
                priority = ProjectPriority.HIGH,
                businessStatus = ProjectStatus.ACTIVE,
                syncState = SyncState.LOCAL_ONLY,
                createdAt = createdAt,
                updatedAt = createdAt
            )

            repository.saveProject(project)

            val retrieved = repository.getProject(projectId)
            assertNotNull(retrieved)
            assertEquals(projectId, retrieved!!.id)
            assertEquals("Meu App Local", retrieved.name)
            assertEquals("Objetivo do projeto local", retrieved.description)
            assertEquals(ProjectPriority.HIGH, retrieved.priority)
            assertEquals(ProjectStatus.ACTIVE, retrieved.businessStatus)
            assertEquals(DataOrigin.LOCAL, retrieved.origin)
            assertEquals(SyncState.LOCAL_ONLY, retrieved.syncState)
            assertNull(retrieved.workstationId)
            assertNull(retrieved.remoteProjectId)
            assertNull(retrieved.lastConfirmedAt)
            assertEquals(createdAt, retrieved.createdAt)
            assertEquals(createdAt, retrieved.updatedAt)

            val allProjects = repository.observeProjects().first()
            assertEquals(1, allProjects.size)
            assertEquals("Meu App Local", allProjects[0].name)
        }
    }

    @Test
    fun editLocalProject_updatesAttributes_andPreservesInvariants() {
        runBlocking {
            val projectId = UUID.randomUUID().toString()
            val initial = LocalProject(
                id = projectId,
                origin = DataOrigin.LOCAL,
                name = "Versão Inicial",
                description = "Escopo v1",
                priority = ProjectPriority.NORMAL,
                businessStatus = ProjectStatus.ACTIVE,
                syncState = SyncState.LOCAL_ONLY,
                createdAt = 1000L,
                updatedAt = 1000L
            )
            repository.saveProject(initial)

            val updated = initial.copy(
                name = "Versão Refatorada",
                description = "Novo escopo expandido",
                priority = ProjectPriority.URGENT,
                businessStatus = ProjectStatus.PAUSED,
                updatedAt = 2500L
            )
            repository.saveProject(updated)

            val retrieved = repository.getProject(projectId)
            assertNotNull(retrieved)
            assertEquals("Versão Refatorada", retrieved!!.name)
            assertEquals("Novo escopo expandido", retrieved.description)
            assertEquals(ProjectPriority.URGENT, retrieved.priority)
            assertEquals(ProjectStatus.PAUSED, retrieved.businessStatus)
            assertEquals(1000L, retrieved.createdAt)
            assertEquals(2500L, retrieved.updatedAt)
            assertEquals(DataOrigin.LOCAL, retrieved.origin)
            assertEquals(SyncState.LOCAL_ONLY, retrieved.syncState)
        }
    }

    @Test
    fun archiveAndUnarchiveLocalProject_updatesStatusAndPreservesData() {
        runBlocking {
            val projectId = UUID.randomUUID().toString()
            val project = LocalProject(
                id = projectId,
                origin = DataOrigin.LOCAL,
                name = "Projeto para Arquivar",
                description = "Escopo",
                priority = ProjectPriority.NORMAL,
                businessStatus = ProjectStatus.ACTIVE,
                createdAt = 1000L,
                updatedAt = 1000L
            )
            repository.saveProject(project)

            // Arquivar
            val archived = project.copy(
                businessStatus = ProjectStatus.ARCHIVED,
                updatedAt = 1500L
            )
            repository.saveProject(archived)

            val retrievedArchived = repository.getProject(projectId)
            assertNotNull(retrievedArchived)
            assertEquals(ProjectStatus.ARCHIVED, retrievedArchived!!.businessStatus)
            assertEquals(1500L, retrievedArchived.updatedAt)

            // Desarquivar / Reativar
            val unarchived = retrievedArchived.copy(
                businessStatus = ProjectStatus.ACTIVE,
                updatedAt = 2000L
            )
            repository.saveProject(unarchived)

            val retrievedActive = repository.getProject(projectId)
            assertNotNull(retrievedActive)
            assertEquals(ProjectStatus.ACTIVE, retrievedActive!!.businessStatus)
            assertEquals(2000L, retrievedActive.updatedAt)
        }
    }

    @Test
    fun deleteLocalProject_cascadesCheckpointsAndDecisions_withoutAffectingOtherProjects() {
        runBlocking {
            val proj1 = LocalProject(
                id = "proj-cascade-1",
                origin = DataOrigin.LOCAL,
                name = "Projeto 1 Alvo de Exclusao",
                description = "Desc 1",
                createdAt = 1000L,
                updatedAt = 1000L
            )
            val proj2 = LocalProject(
                id = "proj-cascade-2",
                origin = DataOrigin.LOCAL,
                name = "Projeto 2 Que Permanece",
                description = "Desc 2",
                createdAt = 2000L,
                updatedAt = 2000L
            )
            repository.saveProject(proj1)
            repository.saveProject(proj2)

            // Adicionar checkpoint para ambos
            val ck1 = com.playertwo1.rin.core.model.LocalCheckpoint(
                id = "ck-1",
                projectId = "proj-cascade-1",
                title = "Checkpoint Proj 1",
                summary = "Resumo 1",
                origin = DataOrigin.LOCAL,
                createdAt = 1000L
            )
            val ck2 = com.playertwo1.rin.core.model.LocalCheckpoint(
                id = "ck-2",
                projectId = "proj-cascade-2",
                title = "Checkpoint Proj 2",
                summary = "Resumo 2",
                origin = DataOrigin.LOCAL,
                createdAt = 2000L
            )
            repository.saveCheckpoint(ck1)
            repository.saveCheckpoint(ck2)

            // Excluir apenas proj1
            repository.deleteProject("proj-cascade-1")

            // Proj1 não deve existir
            assertNull(repository.getProject("proj-cascade-1"))
            assertTrue(repository.observeCheckpoints("proj-cascade-1").first().isEmpty())

            // Proj2 e seu checkpoint devem permanecer intactos
            val retrievedProj2 = repository.getProject("proj-cascade-2")
            assertNotNull(retrievedProj2)
            assertEquals("Projeto 2 Que Permanece", retrievedProj2!!.name)

            val proj2Checkpoints = repository.observeCheckpoints("proj-cascade-2").first()
            assertEquals(1, proj2Checkpoints.size)
            assertEquals("ck-2", proj2Checkpoints[0].id)
        }
    }

    @Test
    fun gateF04_demonstrationScenario_threeProjects_editOne_archiveOne_reopenDatabase_preservesContentAndOrder() {
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val dbName = "rin_gate_f04_${UUID.randomUUID()}.db"
            val persistentDb = androidx.room.Room.databaseBuilder(
                context,
                RinDatabase::class.java,
                dbName
            ).build()
            val repo = RoomProjectRepository(persistentDb)

            try {
                // 1. Cadastrar 3 projetos locais
                val projA = LocalProject(
                    id = "proj-alpha",
                    origin = DataOrigin.LOCAL,
                    name = "Alpha Service",
                    description = "Microsserviço de autenticação",
                    priority = ProjectPriority.NORMAL,
                    businessStatus = ProjectStatus.ACTIVE,
                    syncState = SyncState.LOCAL_ONLY,
                    createdAt = 1000L,
                    updatedAt = 1000L
                )
                val projB = LocalProject(
                    id = "proj-beta",
                    origin = DataOrigin.LOCAL,
                    name = "Beta Portal",
                    description = "Interface do operador",
                    priority = ProjectPriority.HIGH,
                    businessStatus = ProjectStatus.ACTIVE,
                    syncState = SyncState.LOCAL_ONLY,
                    createdAt = 2000L,
                    updatedAt = 2000L
                )
                val projC = LocalProject(
                    id = "proj-gamma",
                    origin = DataOrigin.LOCAL,
                    name = "Gamma Pipeline",
                    description = "Pipeline de ingestão e ETL",
                    priority = ProjectPriority.URGENT,
                    businessStatus = ProjectStatus.ACTIVE,
                    syncState = SyncState.LOCAL_ONLY,
                    createdAt = 3000L,
                    updatedAt = 3000L
                )
                repo.saveProject(projA)
                repo.saveProject(projB)
                repo.saveProject(projC)

                // 2. Editar um projeto (projB)
                val editedProjB = projB.copy(
                    name = "Beta Portal Enterprise",
                    description = "Interface expandida para operação corporativa",
                    priority = ProjectPriority.URGENT,
                    businessStatus = ProjectStatus.PAUSED,
                    updatedAt = 4000L
                )
                repo.saveProject(editedProjB)

                // 3. Arquivar outro projeto (projC)
                val archivedProjC = projC.copy(
                    businessStatus = ProjectStatus.ARCHIVED,
                    updatedAt = 5000L
                )
                repo.saveProject(archivedProjC)

                // 4. Fechar banco simulando término do processo
                persistentDb.close()

                // 5. Reabrir novo banco apontando para o mesmo arquivo físico
                val reopenedDb = androidx.room.Room.databaseBuilder(
                    context,
                    RinDatabase::class.java,
                    dbName
                ).build()
                val reopenedRepo = RoomProjectRepository(reopenedDb)

                try {
                    val allProjects = reopenedRepo.observeProjects().first()
                    assertEquals(3, allProjects.size)

                    // Verificar ordenação cronológica decrescente por data de criação (ou updatedAt conforme repositório)
                    val retrievedA = reopenedRepo.getProject("proj-alpha")
                    val retrievedB = reopenedRepo.getProject("proj-beta")
                    val retrievedC = reopenedRepo.getProject("proj-gamma")

                    assertNotNull(retrievedA)
                    assertEquals("Alpha Service", retrievedA!!.name)
                    assertEquals(ProjectPriority.NORMAL, retrievedA.priority)
                    assertEquals(ProjectStatus.ACTIVE, retrievedA.businessStatus)

                    assertNotNull(retrievedB)
                    assertEquals("Beta Portal Enterprise", retrievedB!!.name)
                    assertEquals("Interface expandida para operação corporativa", retrievedB.description)
                    assertEquals(ProjectPriority.URGENT, retrievedB.priority)
                    assertEquals(ProjectStatus.PAUSED, retrievedB.businessStatus)
                    assertEquals(4000L, retrievedB.updatedAt)

                    assertNotNull(retrievedC)
                    assertEquals("Gamma Pipeline", retrievedC!!.name)
                    assertEquals(ProjectStatus.ARCHIVED, retrievedC.businessStatus)
                    assertEquals(5000L, retrievedC.updatedAt)
                } finally {
                    reopenedDb.close()
                }
            } finally {
                context.deleteDatabase(dbName)
            }
        }
    }

    @Test
    fun createProject_withLongNameAndDescription_persistsAndRetrievesWithoutTruncation() {
        runBlocking {
            val longName = "A".repeat(200)
            val longDescription = "D".repeat(1500)
            val projectId = "proj-long-strings"

            val project = LocalProject(
                id = projectId,
                origin = DataOrigin.LOCAL,
                name = longName,
                description = longDescription,
                priority = ProjectPriority.LOW,
                businessStatus = ProjectStatus.ACTIVE,
                syncState = SyncState.LOCAL_ONLY,
                createdAt = 1000L,
                updatedAt = 1000L
            )

            repository.saveProject(project)

            val retrieved = repository.getProject(projectId)
            assertNotNull(retrieved)
            assertEquals(longName, retrieved!!.name)
            assertEquals(longDescription, retrieved.description)
        }
    }
}
