package com.playertwo1.rin

import com.playertwo1.rin.core.model.DataOrigin
import com.playertwo1.rin.core.model.LocalCheckpoint
import com.playertwo1.rin.core.model.LocalDecisionDraft
import com.playertwo1.rin.core.model.LocalProject
import com.playertwo1.rin.core.model.ProjectStatus
import com.playertwo1.rin.core.model.SyncState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class IdentityAndInvariantsTest {

    @Test
    fun projects_from_two_workstations_with_same_remote_id_do_not_collide() {
        val remoteId = "projeto-compartilhado-1"
        val now = 1700000000000L

        val projectWorkstationA = LocalProject(
            id = UUID.randomUUID().toString(),
            workstationId = "workstation-notebook",
            remoteProjectId = remoteId,
            origin = DataOrigin.WORKSTATION_REMOTE,
            name = "Projeto no Notebook",
            description = "Ambiente local da workstation A",
            createdAt = now,
            updatedAt = now,
            lastConfirmedAt = now
        )

        val projectWorkstationB = LocalProject(
            id = UUID.randomUUID().toString(),
            workstationId = "workstation-servidor-lan",
            remoteProjectId = remoteId,
            origin = DataOrigin.WORKSTATION_REMOTE,
            name = "Projeto no Servidor",
            description = "Ambiente da workstation B",
            createdAt = now,
            updatedAt = now,
            lastConfirmedAt = now
        )

        // As identidades locais são estritamente distintas
        assertNotEquals(projectWorkstationA.id, projectWorkstationB.id)

        // As chaves compostas de origem remota são distintas mesmo com mesmo remoteId
        assertEquals("workstation-notebook:$remoteId", projectWorkstationA.remoteCompositeKey)
        assertEquals("workstation-servidor-lan:$remoteId", projectWorkstationB.remoteCompositeKey)
        assertNotEquals(projectWorkstationA.remoteCompositeKey, projectWorkstationB.remoteCompositeKey)
    }

    @Test
    fun absent_quota_and_test_status_remain_null_unknown() {
        val now = 1700000000000L
        val project = LocalProject(
            id = UUID.randomUUID().toString(),
            origin = DataOrigin.LOCAL,
            name = "Projeto Offline",
            description = "Criado sem conexão",
            createdAt = now,
            updatedAt = now
        )

        // Invariante de honestidade: cota e teste ausentes NÃO devem ser inventados como 0 ou sucesso
        assertNull("Cota ausente deve permanecer null (desconhecida)", project.quotaUsagePercent)
        assertNull("Status de teste ausente deve permanecer null (desconhecido)", project.testRunStatus)
    }

    @Test(expected = IllegalArgumentException::class)
    fun checkpoint_without_project_id_fails_validation() {
        LocalCheckpoint(
            id = UUID.randomUUID().toString(),
            projectId = "", // Inválido: checkpoint exige vínculo a projeto existente
            title = "Checkpoint Órfão",
            summary = "Tentativa inválida de criar checkpoint sem projeto",
            createdAt = 1700000000000L
        )
    }

    @Test
    fun business_status_and_sync_state_are_strictly_decoupled() {
        val now = 1700000000000L
        val conflictedFinishedProject = LocalProject(
            id = UUID.randomUUID().toString(),
            workstationId = "ws-main",
            remoteProjectId = "remote-123",
            origin = DataOrigin.WORKSTATION_REMOTE,
            name = "Projeto Concluído com Conflito",
            description = "Concluído localmente porém divergente na rede",
            businessStatus = ProjectStatus.COMPLETED,
            syncState = SyncState.CONFLICT,
            createdAt = now,
            updatedAt = now
        )

        assertEquals(ProjectStatus.COMPLETED, conflictedFinishedProject.businessStatus)
        assertEquals(SyncState.CONFLICT, conflictedFinishedProject.syncState)
    }

    @Test
    fun local_project_can_exist_offline_without_workstation() {
        val now = 1700000000000L
        val localOnlyProject = LocalProject(
            id = UUID.randomUUID().toString(),
            workstationId = null,
            remoteProjectId = null,
            origin = DataOrigin.LOCAL,
            name = "Meu Projeto Local",
            description = "Totalmente offline",
            createdAt = now,
            updatedAt = now
        )

        assertEquals(DataOrigin.LOCAL, localOnlyProject.origin)
        assertEquals(SyncState.LOCAL_ONLY, localOnlyProject.syncState)
        assertNull(localOnlyProject.remoteCompositeKey)
    }

    @Test
    fun decision_draft_validates_invariants() {
        val now = 1700000000000L
        val draft = LocalDecisionDraft(
            id = UUID.randomUUID().toString(),
            projectId = "proj-xyz",
            question = "Qual estratégia de cache?",
            options = listOf("Memory", "Room SQLite"),
            selectedOption = "Room SQLite",
            isConfirmedLocally = true,
            createdAt = now,
            updatedAt = now
        )

        assertEquals("proj-xyz", draft.projectId)
        assertEquals("Room SQLite", draft.selectedOption)
        assertTrue(draft.isConfirmedLocally)
    }
}
