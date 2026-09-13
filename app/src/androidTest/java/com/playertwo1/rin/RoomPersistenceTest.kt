package com.playertwo1.rin

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.playertwo1.rin.core.database.RinDatabase
import com.playertwo1.rin.core.database.repository.RoomProjectRepository
import com.playertwo1.rin.core.model.DataOrigin
import com.playertwo1.rin.core.model.LocalCheckpoint
import com.playertwo1.rin.core.model.LocalDecisionDraft
import com.playertwo1.rin.core.model.LocalProject
import com.playertwo1.rin.core.model.ProjectStatus
import com.playertwo1.rin.core.model.SyncState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class RoomPersistenceTest {

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
    fun insert_update_and_read_projects_maintains_ordering_and_data() = runBlocking {
        val p1 = LocalProject(
            id = "proj-1",
            origin = DataOrigin.LOCAL,
            name = "Projeto Primeiro",
            description = "Descricao 1",
            createdAt = 1000L,
            updatedAt = 1000L
        )
        val p2 = LocalProject(
            id = "proj-2",
            origin = DataOrigin.LOCAL,
            name = "Projeto Segundo (Mais Recente)",
            description = "Descricao 2",
            createdAt = 2000L,
            updatedAt = 2000L
        )

        repository.saveProject(p1)
        repository.saveProject(p2)

        // Observar lista completa - ordenada por updatedAt DESC
        val projects = repository.observeProjects().first()
        assertEquals(2, projects.size)
        assertEquals("proj-2", projects[0].id)
        assertEquals("proj-1", projects[1].id)

        // Atualizar p1 com timestamp mais recente
        val p1Updated = p1.copy(updatedAt = 3000L, name = "Projeto Primeiro Atualizado")
        repository.saveProject(p1Updated)

        val projectsAfterUpdate = repository.observeProjects().first()
        assertEquals(2, projectsAfterUpdate.size)
        assertEquals("proj-1", projectsAfterUpdate[0].id)
        assertEquals("Projeto Primeiro Atualizado", projectsAfterUpdate[0].name)
    }

    @Test
    fun checkpoints_are_ordered_and_latest_is_retrieved_correctly() = runBlocking {
        val project = LocalProject(
            id = "proj-ck",
            origin = DataOrigin.LOCAL,
            name = "Projeto com Checkpoints",
            description = "Teste de checkpoints",
            createdAt = 1000L,
            updatedAt = 1000L
        )
        repository.saveProject(project)

        val ck1 = LocalCheckpoint(
            id = "ck-1",
            projectId = "proj-ck",
            title = "Etapa 1",
            summary = "Fundação feita",
            createdAt = 1000L
        )
        val ck2 = LocalCheckpoint(
            id = "ck-2",
            projectId = "proj-ck",
            title = "Etapa 2",
            summary = "DAOs implementados",
            createdAt = 2000L
        )

        repository.saveCheckpoint(ck1)
        repository.saveCheckpoint(ck2)

        val latest = repository.getLatestCheckpoint("proj-ck")
        assertNotNull(latest)
        assertEquals("ck-2", latest?.id)
        assertEquals("DAOs implementados", latest?.summary)

        val allCheckpoints = repository.observeCheckpoints("proj-ck").first()
        assertEquals(2, allCheckpoints.size)
        assertEquals("ck-2", allCheckpoints[0].id)
        assertEquals("ck-1", allCheckpoints[1].id)
    }

    @Test
    fun deleting_project_cascades_and_deletes_associated_checkpoints() = runBlocking {
        val project = LocalProject(
            id = "proj-del",
            origin = DataOrigin.LOCAL,
            name = "Projeto a deletar",
            description = "Verificar cascade",
            createdAt = 1000L,
            updatedAt = 1000L
        )
        repository.saveProject(project)

        val checkpoint = LocalCheckpoint(
            id = "ck-del",
            projectId = "proj-del",
            title = "Etapa temporária",
            summary = "Deve sumir com cascade",
            createdAt = 1000L
        )
        repository.saveCheckpoint(checkpoint)

        // Deletar o projeto
        repository.deleteProject("proj-del")

        assertNull(repository.getProject("proj-del"))
        val remainingCheckpoints = repository.observeCheckpoints("proj-del").first()
        assertTrue(remainingCheckpoints.isEmpty())
    }

    @Test
    fun transaction_failure_does_not_leave_partial_records() = runBlocking {
        val project = LocalProject(
            id = "proj-atomic-ok",
            origin = DataOrigin.LOCAL,
            name = "Projeto Atômico",
            description = "Teste de atomicidade",
            createdAt = 1000L,
            updatedAt = 1000L
        )
        val validCheckpoint = LocalCheckpoint(
            id = "ck-atomic-ok",
            projectId = "proj-atomic-ok",
            title = "Etapa Ok",
            summary = "Tudo certo",
            createdAt = 1000L
        )

        repository.saveProjectWithCheckpointAtomic(project, validCheckpoint)
        assertNotNull(repository.getProject("proj-atomic-ok"))
        assertNotNull(repository.getLatestCheckpoint("proj-atomic-ok"))

        // Simular falha transacional: tentar inserir projeto novo com checkpoint apontando
        // para projeto inexistente diretamente via foreign key violation na transação
        val newProject = LocalProject(
            id = "proj-must-rollback",
            origin = DataOrigin.LOCAL,
            name = "Projeto que deve ser desfeito",
            description = "Não deve persistir se falhar",
            createdAt = 2000L,
            updatedAt = 2000L
        )
        val invalidCheckpointWithNonExistentFk = LocalCheckpoint(
            id = "ck-fail-fk",
            projectId = "projeto-que-nao-existe", // Violação de Foreign Key no SQLite
            title = "Etapa Inválida",
            summary = "Falhará na FK",
            createdAt = 2000L
        )

        try {
            repository.saveProjectWithCheckpointAtomic(newProject, invalidCheckpointWithNonExistentFk)
            fail("Deveria lançar exceção de violação de chave estrangeira")
        } catch (e: Exception) {
            // Esperado erro de integridade referencial
        }

        // Prova de atomicidade: o projeto newProject NÃO foi persistido (rollback completo)
        assertNull("Projeto não deve existir no banco após falha na transação", repository.getProject("proj-must-rollback"))
    }

    @Test
    fun two_workstations_with_same_remote_id_can_coexist() = runBlocking {
        val pAlpha = LocalProject(
            id = "local-uuid-alpha",
            workstationId = "station-alpha",
            remoteProjectId = "proj-shared",
            origin = DataOrigin.WORKSTATION_REMOTE,
            name = "Projeto Alfa",
            description = "Em alfa",
            createdAt = 1000L,
            updatedAt = 1000L
        )
        val pBeta = LocalProject(
            id = "local-uuid-beta",
            workstationId = "station-beta",
            remoteProjectId = "proj-shared",
            origin = DataOrigin.WORKSTATION_REMOTE,
            name = "Projeto Beta",
            description = "Em beta",
            createdAt = 2000L,
            updatedAt = 2000L
        )

        repository.saveProjects(listOf(pAlpha, pBeta))

        val retrievedAlpha = repository.getProjectByRemoteIdentity("station-alpha", "proj-shared")
        val retrievedBeta = repository.getProjectByRemoteIdentity("station-beta", "proj-shared")

        assertNotNull(retrievedAlpha)
        assertNotNull(retrievedBeta)
        assertEquals("local-uuid-alpha", retrievedAlpha?.id)
        assertEquals("local-uuid-beta", retrievedBeta?.id)
        assertEquals("Projeto Alfa", retrievedAlpha?.name)
        assertEquals("Projeto Beta", retrievedBeta?.name)
    }
}
