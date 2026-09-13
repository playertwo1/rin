package com.playertwo1.rin.core.network.fake

import com.playertwo1.rin.core.model.ErrorCategory
import com.playertwo1.rin.core.model.NetworkResult
import com.playertwo1.rin.core.model.ProjectCheckpoint
import com.playertwo1.rin.core.model.ProjectDetail
import com.playertwo1.rin.core.model.ProjectSummary
import com.playertwo1.rin.core.model.WorkstationError
import com.playertwo1.rin.core.model.WorkstationEvent
import com.playertwo1.rin.core.model.WorkstationHealth
import com.playertwo1.rin.core.network.WorkstationGateway
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class FakeScenario {
    DEFAULT,
    EMPTY,
    OFFLINE,
    SERVER_ERROR,
    STALE_DATA,
    CAPABILITY_MISSING,
    INCOMPATIBLE_VERSION,
    REPEATED_EVENTS
}

/**
 * Implementação fake determinística e controlável do WorkstationGateway.
 * Projetada para testes, simulação e desenvolvimento sem depender da AI Workstation física.
 */
class FakeWorkstationGateway(
    initialScenario: FakeScenario = FakeScenario.DEFAULT
) : WorkstationGateway {

    private val _currentScenario = MutableStateFlow(initialScenario)
    val currentScenario: StateFlow<FakeScenario> = _currentScenario.asStateFlow()

    fun setScenario(scenario: FakeScenario) {
        _currentScenario.value = scenario
    }

    fun reset() {
        _currentScenario.value = FakeScenario.DEFAULT
    }

    override suspend fun getHealth(): NetworkResult<WorkstationHealth> {
        return when (_currentScenario.value) {
            FakeScenario.OFFLINE -> NetworkResult.Error(
                WorkstationError(
                    code = "ERR_NET_OFFLINE",
                    category = ErrorCategory.NETWORK_UNAVAILABLE,
                    message = "Host da AI Workstation inalcançável na rede local."
                )
            )
            FakeScenario.SERVER_ERROR -> NetworkResult.Error(
                WorkstationError(
                    code = "ERR_INTERNAL_500",
                    category = ErrorCategory.SERVER_ERROR,
                    message = "Falha interna no serviço da AI Workstation."
                )
            )
            FakeScenario.INCOMPATIBLE_VERSION -> NetworkResult.Error(
                WorkstationError(
                    code = "ERR_API_VERSION",
                    category = ErrorCategory.INCOMPATIBLE_VERSION,
                    message = "Versão do contrato incompatível. Esperada v1, recebida v0."
                )
            )
            FakeScenario.CAPABILITY_MISSING -> NetworkResult.Success(
                WorkstationHealth(
                    status = "healthy",
                    version = "0.1.0-alpha",
                    workstationName = "AI-Workstation-Simulada",
                    capabilities = setOf("health.read") // capabilities ausentes
                )
            )
            else -> NetworkResult.Success(
                WorkstationHealth(
                    status = "healthy",
                    version = "0.1.0-alpha",
                    workstationName = "AI-Workstation-Simulada",
                    capabilities = setOf("projects.read", "projects.write", "events.stream")
                )
            )
        }
    }

    override suspend fun getProjects(): NetworkResult<List<ProjectSummary>> {
        return when (_currentScenario.value) {
            FakeScenario.EMPTY -> NetworkResult.Success(emptyList())
            FakeScenario.OFFLINE -> NetworkResult.Error(
                WorkstationError(
                    code = "ERR_NET_OFFLINE",
                    category = ErrorCategory.NETWORK_UNAVAILABLE,
                    message = "Não foi possível carregar projetos: sem conexão na LAN."
                )
            )
            FakeScenario.SERVER_ERROR -> NetworkResult.Error(
                WorkstationError(
                    code = "ERR_INTERNAL_500",
                    category = ErrorCategory.SERVER_ERROR,
                    message = "Erro interno ao consultar lista de projetos."
                )
            )
            FakeScenario.CAPABILITY_MISSING -> NetworkResult.Error(
                WorkstationError(
                    code = "ERR_CAPABILITY_MISSING",
                    category = ErrorCategory.CAPABILITY_MISSING,
                    message = "A workstation conectada não possui a capacidade 'projects.read'."
                )
            )
            else -> NetworkResult.Success(
                listOf(
                    ProjectSummary(
                        id = "proj-rin-01",
                        name = "rin",
                        description = "Control Plane Android para a AI Workstation",
                        currentBranch = "main",
                        status = "active",
                        updatedAt = "2026-09-13T09:30:00Z"
                    ),
                    ProjectSummary(
                        id = "proj-aiworkstation-02",
                        name = "aiworkstation",
                        description = "Plataforma central de orquestração de agentes e execução local",
                        currentBranch = "develop",
                        status = "idle",
                        updatedAt = "2026-09-13T08:15:00Z"
                    ),
                    ProjectSummary(
                        id = "proj-hermes-03",
                        name = "hermes-agent",
                        description = "Adaptador de mensageria e execução autônoma",
                        currentBranch = "main",
                        status = "running",
                        updatedAt = "2026-09-13T07:45:00Z"
                    )
                )
            )
        }
    }

    override suspend fun getProjectDetail(projectId: String): NetworkResult<ProjectDetail> {
        if (_currentScenario.value == FakeScenario.OFFLINE) {
            return NetworkResult.Error(
                WorkstationError(
                    code = "ERR_NET_OFFLINE",
                    category = ErrorCategory.NETWORK_UNAVAILABLE,
                    message = "Sem conexão na LAN para carregar detalhes."
                )
            )
        }
        if (projectId != "proj-rin-01") {
            return NetworkResult.Error(
                WorkstationError(
                    code = "PROJECT_NOT_FOUND",
                    category = ErrorCategory.NOT_FOUND,
                    message = "Projeto '$projectId' não encontrado na workstation."
                )
            )
        }
        return NetworkResult.Success(
            ProjectDetail(
                id = "proj-rin-01",
                name = "rin",
                description = "Control Plane Android para a AI Workstation",
                currentBranch = "main",
                status = "active",
                lastCheckpoint = ProjectCheckpoint(
                    id = "chk-001",
                    title = "Scaffold F01 validado em emulador",
                    summary = "Implementação do scaffold Android com Compose e testes unitários aprovados",
                    createdAt = "2026-09-13T09:30:00Z"
                ),
                gitCommitHash = "cd49296",
                openDecisionsCount = 7,
                updatedAt = "2026-09-13T09:30:00Z"
            )
        )
    }

    override suspend fun getEvents(cursor: String?): NetworkResult<List<WorkstationEvent>> {
        if (_currentScenario.value == FakeScenario.OFFLINE) {
            return NetworkResult.Error(
                WorkstationError(
                    code = "ERR_NET_OFFLINE",
                    category = ErrorCategory.NETWORK_UNAVAILABLE,
                    message = "Sem conexão para streaming de eventos."
                )
            )
        }
        if (_currentScenario.value == FakeScenario.REPEATED_EVENTS) {
            return NetworkResult.Success(
                listOf(
                    WorkstationEvent("evt-dup", "c-dup", "proj-rin-01", "checkpoint_created", "Checkpoint duplicado", "2026-09-13T09:00:00Z"),
                    WorkstationEvent("evt-dup", "c-dup", "proj-rin-01", "checkpoint_created", "Checkpoint duplicado", "2026-09-13T09:00:00Z")
                )
            )
        }
        return NetworkResult.Success(
            listOf(
                WorkstationEvent("evt-001", "c-001", "proj-rin-01", "checkpoint_created", "Novo checkpoint criado", "2026-09-13T09:30:00Z"),
                WorkstationEvent("evt-002", "c-002", "proj-hermes-03", "session_started", "Sessão iniciada", "2026-09-13T09:35:00Z")
            )
        )
    }
}
