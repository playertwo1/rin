package com.playertwo1.rin

import com.playertwo1.rin.core.database.mapper.toDomain
import com.playertwo1.rin.core.database.mapper.toEntity
import com.playertwo1.rin.core.model.DataOrigin
import com.playertwo1.rin.core.model.LocalCheckpoint
import com.playertwo1.rin.core.model.LocalDecisionDraft
import com.playertwo1.rin.core.model.LocalProject
import com.playertwo1.rin.core.model.ProjectStatus
import com.playertwo1.rin.core.model.SyncState
import com.playertwo1.rin.core.model.WorkstationMetadata
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class DatabaseMappersTest {

    @Test
    fun project_mapping_roundtrip_preserves_all_fields_and_nulls() {
        val now = 1700000000000L
        val original = LocalProject(
            id = UUID.randomUUID().toString(),
            workstationId = "ws-remote-1",
            remoteProjectId = "rin-core",
            origin = DataOrigin.WORKSTATION_REMOTE,
            name = "RIN Core Android",
            description = "App nativo Android",
            currentBranch = "main",
            gitCommitHash = "abcdef0123456789",
            businessStatus = ProjectStatus.ACTIVE,
            syncState = SyncState.SYNCED,
            quotaUsagePercent = null, // Invariante: desconhecido permanece null
            testRunStatus = null,     // Invariante: desconhecido permanece null
            createdAt = now,
            updatedAt = now,
            lastConfirmedAt = now
        )

        val entity = original.toEntity()
        val mappedBack = entity.toDomain()

        assertEquals(original.id, mappedBack.id)
        assertEquals(original.workstationId, mappedBack.workstationId)
        assertEquals(original.remoteProjectId, mappedBack.remoteProjectId)
        assertEquals(original.origin, mappedBack.origin)
        assertEquals(original.name, mappedBack.name)
        assertEquals(original.description, mappedBack.description)
        assertEquals(original.currentBranch, mappedBack.currentBranch)
        assertEquals(original.gitCommitHash, mappedBack.gitCommitHash)
        assertEquals(original.businessStatus, mappedBack.businessStatus)
        assertEquals(original.syncState, mappedBack.syncState)
        assertNull(mappedBack.quotaUsagePercent)
        assertNull(mappedBack.testRunStatus)
        assertEquals(original.createdAt, mappedBack.createdAt)
        assertEquals(original.updatedAt, mappedBack.updatedAt)
        assertEquals(original.lastConfirmedAt, mappedBack.lastConfirmedAt)
    }

    @Test
    fun checkpoint_mapping_roundtrip_preserves_data() {
        val now = 1700000000000L
        val original = LocalCheckpoint(
            id = UUID.randomUUID().toString(),
            projectId = "proj-123",
            title = "Scaffold Concluído",
            summary = "Estrutura do banco e DAOs criada",
            nextStep = "Persistir dados de teste",
            blockers = null,
            referencesText = "docs/roadmap/F03-persistencia.md",
            origin = DataOrigin.LOCAL,
            createdAt = now,
            lastConfirmedAt = null
        )

        val entity = original.toEntity()
        val mappedBack = entity.toDomain()

        assertEquals(original.id, mappedBack.id)
        assertEquals(original.projectId, mappedBack.projectId)
        assertEquals(original.title, mappedBack.title)
        assertEquals(original.summary, mappedBack.summary)
        assertEquals(original.nextStep, mappedBack.nextStep)
        assertNull(mappedBack.blockers)
        assertEquals(original.referencesText, mappedBack.referencesText)
        assertEquals(original.origin, mappedBack.origin)
        assertEquals(original.createdAt, mappedBack.createdAt)
        assertNull(mappedBack.lastConfirmedAt)
    }

    @Test
    fun decision_draft_mapping_roundtrip_serializes_options_list() {
        val now = 1700000000000L
        val original = LocalDecisionDraft(
            id = UUID.randomUUID().toString(),
            projectId = "proj-123",
            question = "Estratégia de concorrência?",
            options = listOf("Room + Flow", "StateFlow em memória", "LiveQuery"),
            selectedOption = "Room + Flow",
            isConfirmedLocally = true,
            createdAt = now,
            updatedAt = now
        )

        val entity = original.toEntity()
        val mappedBack = entity.toDomain()

        assertEquals(original.id, mappedBack.id)
        assertEquals(original.projectId, mappedBack.projectId)
        assertEquals(original.question, mappedBack.question)
        assertEquals(3, mappedBack.options.size)
        assertEquals("Room + Flow", mappedBack.options[0])
        assertEquals("StateFlow em memória", mappedBack.options[1])
        assertEquals("LiveQuery", mappedBack.options[2])
        assertEquals("Room + Flow", mappedBack.selectedOption)
        assertTrue(mappedBack.isConfirmedLocally)
    }

    @Test
    fun workstation_metadata_mapping_roundtrip_serializes_capabilities() {
        val original = WorkstationMetadata(
            workstationId = "ws-desktop-01",
            workstationName = "Workstation Escritório",
            version = "1.0.0",
            status = "HEALTHY",
            capabilities = setOf("projects.read", "projects.write", "events.stream"),
            lastConfirmedAt = 1700000000000L
        )

        val entity = original.toEntity()
        val mappedBack = entity.toDomain()

        assertEquals(original.workstationId, mappedBack.workstationId)
        assertEquals(original.workstationName, mappedBack.workstationName)
        assertEquals(original.version, mappedBack.version)
        assertEquals(original.status, mappedBack.status)
        assertEquals(original.capabilities, mappedBack.capabilities)
        assertEquals(original.lastConfirmedAt, mappedBack.lastConfirmedAt)
    }
}
