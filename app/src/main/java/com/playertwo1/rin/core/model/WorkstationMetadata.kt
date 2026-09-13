package com.playertwo1.rin.core.model

data class WorkstationMetadata(
    val workstationId: String,
    val workstationName: String,
    val version: String,
    val status: String,
    val capabilities: Set<String>,
    val lastConfirmedAt: Long
) {
    init {
        require(workstationId.isNotBlank()) { "workstationId não pode ser vazio" }
        require(workstationName.isNotBlank()) { "workstationName não pode ser vazio" }
        require(version.isNotBlank()) { "version não pode ser vazio" }
    }
}
