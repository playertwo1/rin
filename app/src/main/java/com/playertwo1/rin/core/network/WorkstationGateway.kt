package com.playertwo1.rin.core.network

import com.playertwo1.rin.core.model.NetworkResult
import com.playertwo1.rin.core.model.ProjectDetail
import com.playertwo1.rin.core.model.ProjectSummary
import com.playertwo1.rin.core.model.WorkstationEvent
import com.playertwo1.rin.core.model.WorkstationHealth

/**
 * Fronteira canônica e obrigatória de comunicação entre o aplicativo RIN
 * e a AI Workstation, conforme decisão D008 e ADR 0001/0002.
 */
interface WorkstationGateway {
    /**
     * Consulta saúde, versão da API e capabilities anunciadas pela workstation.
     */
    suspend fun getHealth(): NetworkResult<WorkstationHealth>

    /**
     * Lista projetos autorizados na AI Workstation.
     */
    suspend fun getProjects(): NetworkResult<List<ProjectSummary>>

    /**
     * Obtém detalhes e último checkpoint de um projeto específico.
     */
    suspend fun getProjectDetail(projectId: String): NetworkResult<ProjectDetail>

    /**
     * Consulta eventos incrementais a partir de um cursor opaco.
     */
    suspend fun getEvents(cursor: String? = null): NetworkResult<List<WorkstationEvent>>
}
