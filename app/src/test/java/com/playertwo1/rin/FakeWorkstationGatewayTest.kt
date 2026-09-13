package com.playertwo1.rin

import com.playertwo1.rin.core.model.ErrorCategory
import com.playertwo1.rin.core.model.NetworkResult
import com.playertwo1.rin.core.network.fake.FakeScenario
import com.playertwo1.rin.core.network.fake.FakeWorkstationGateway
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeWorkstationGatewayTest {

    private lateinit var gateway: FakeWorkstationGateway

    @Before
    fun setup() {
        gateway = FakeWorkstationGateway()
    }

    @Test
    fun defaultScenario_returnsHealthyAndThreeProjects() = runBlocking {
        val healthResult = gateway.getHealth()
        assertTrue(healthResult is NetworkResult.Success)
        val health = (healthResult as NetworkResult.Success).data
        assertTrue(health.isHealthy)
        assertTrue(health.hasCapability("projects.read"))

        val projectsResult = gateway.getProjects()
        assertTrue(projectsResult is NetworkResult.Success)
        val projects = (projectsResult as NetworkResult.Success).data
        assertEquals(3, projects.size)
        assertEquals("rin", projects[0].name)

        val detailResult = gateway.getProjectDetail("proj-rin-01")
        assertTrue(detailResult is NetworkResult.Success)
        val detail = (detailResult as NetworkResult.Success).data
        assertEquals("proj-rin-01", detail.id)
        assertEquals("chk-001", detail.lastCheckpoint?.id)
    }

    @Test
    fun emptyScenario_returnsEmptyProjectList() = runBlocking {
        gateway.setScenario(FakeScenario.EMPTY)
        val projectsResult = gateway.getProjects()
        assertTrue(projectsResult is NetworkResult.Success)
        val projects = (projectsResult as NetworkResult.Success).data
        assertTrue(projects.isEmpty())
    }

    @Test
    fun offlineScenario_returnsNetworkUnavailableError() = runBlocking {
        gateway.setScenario(FakeScenario.OFFLINE)

        val healthResult = gateway.getHealth()
        assertTrue(healthResult is NetworkResult.Error)
        val error = (healthResult as NetworkResult.Error).error
        assertEquals(ErrorCategory.NETWORK_UNAVAILABLE, error.category)

        val projectsResult = gateway.getProjects()
        assertTrue(projectsResult is NetworkResult.Error)
    }

    @Test
    fun serverErrorScenario_returnsServerError() = runBlocking {
        gateway.setScenario(FakeScenario.SERVER_ERROR)
        val healthResult = gateway.getHealth()
        assertTrue(healthResult is NetworkResult.Error)
        val error = (healthResult as NetworkResult.Error).error
        assertEquals(ErrorCategory.SERVER_ERROR, error.category)
    }

    @Test
    fun capabilityMissingScenario_failsWhenRequestingMissingCapability() = runBlocking {
        gateway.setScenario(FakeScenario.CAPABILITY_MISSING)
        val projectsResult = gateway.getProjects()
        assertTrue(projectsResult is NetworkResult.Error)
        val error = (projectsResult as NetworkResult.Error).error
        assertEquals(ErrorCategory.CAPABILITY_MISSING, error.category)
    }

    @Test
    fun reset_restoresDefaultState() = runBlocking {
        gateway.setScenario(FakeScenario.OFFLINE)
        gateway.reset()
        val health = gateway.getHealth()
        assertTrue(health is NetworkResult.Success)
    }
}
