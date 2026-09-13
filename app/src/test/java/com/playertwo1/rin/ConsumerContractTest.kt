package com.playertwo1.rin

import com.playertwo1.rin.core.model.ErrorCategory
import com.playertwo1.rin.core.network.dto.EnvelopeDto
import com.playertwo1.rin.core.network.dto.EventDto
import com.playertwo1.rin.core.network.dto.HealthDto
import com.playertwo1.rin.core.network.dto.ProjectDetailDto
import com.playertwo1.rin.core.network.dto.ProjectSummaryDto
import com.playertwo1.rin.core.network.mapper.toDomain
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class ConsumerContractTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private fun loadFixture(filename: String): String {
        val pathCandidates = listOf(
            File("contracts/fixtures", filename),
            File("../contracts/fixtures", filename),
            File("../../contracts/fixtures", filename)
        )
        val file = pathCandidates.firstOrNull { it.exists() }
            ?: throw IllegalArgumentException("Fixture $filename não encontrada em ${pathCandidates.map { it.absolutePath }}")
        return file.readText()
    }

    @Test
    fun testHealthSuccessFixture_deserializesAndMapsToDomain() {
        val raw = loadFixture("health-success.json")
        val envelope = json.decodeFromString<EnvelopeDto<HealthDto>>(raw)

        assertEquals("1.0", envelope.schemaVersion)
        assertNull(envelope.error)
        assertNotNull(envelope.data)

        val domain = envelope.data!!.toDomain()
        assertTrue(domain.isHealthy)
        assertEquals("0.1.0-alpha", domain.version)
        assertEquals("AI-Workstation-LAN", domain.workstationName)
        assertTrue(domain.hasCapability("projects.read"))
    }

    @Test
    fun testProjectsListSuccessFixture_deserializesAndMapsToDomain() {
        val raw = loadFixture("projects-list-success.json")
        val envelope = json.decodeFromString<EnvelopeDto<List<ProjectSummaryDto>>>(raw)

        assertEquals("1.0", envelope.schemaVersion)
        assertNull(envelope.error)
        assertEquals(3, envelope.data?.size)

        val domainProjects = envelope.data!!.map { it.toDomain() }
        val rin = domainProjects.first { it.id == "proj-rin-01" }
        assertEquals("rin", rin.name)
        assertEquals("main", rin.currentBranch)
        assertEquals("active", rin.status)
    }

    @Test
    fun testProjectDetailSuccessFixture_deserializesAndMapsToDomain() {
        val raw = loadFixture("project-detail-success.json")
        val envelope = json.decodeFromString<EnvelopeDto<ProjectDetailDto>>(raw)

        assertNotNull(envelope.data)
        val detail = envelope.data!!.toDomain()
        assertEquals("proj-rin-01", detail.id)
        assertNotNull(detail.lastCheckpoint)
        assertEquals("chk-001", detail.lastCheckpoint?.id)
        assertEquals("cd49296", detail.gitCommitHash)
        assertEquals(7, detail.openDecisionsCount)
    }

    @Test
    fun testEventsSuccessFixture_deserializesAndMapsToDomain() {
        val raw = loadFixture("events-success.json")
        val envelope = json.decodeFromString<EnvelopeDto<List<EventDto>>>(raw)

        assertEquals(2, envelope.data?.size)
        val events = envelope.data!!.map { it.toDomain() }
        assertEquals("c-001", events[0].cursor)
        assertEquals("checkpoint_created", events[0].type)
    }

    @Test
    fun testErrorResponseFixture_deserializesStructuredError() {
        val raw = loadFixture("error-response.json")
        val envelope = json.decodeFromString<EnvelopeDto<String>>(raw)

        assertNull(envelope.data)
        assertNotNull(envelope.error)

        val domainError = envelope.error!!.toDomain()
        assertEquals("PROJECT_NOT_FOUND", domainError.code)
        assertEquals(ErrorCategory.NOT_FOUND, domainError.category)
        assertTrue(domainError.message.contains("não foi localizado"))
    }
}
