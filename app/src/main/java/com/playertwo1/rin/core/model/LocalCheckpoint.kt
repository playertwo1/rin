package com.playertwo1.rin.core.model

data class LocalCheckpoint(
    val id: String,
    val projectId: String,
    val title: String,
    val summary: String,
    val nextStep: String? = null,
    val blockers: String? = null,
    val referencesText: String? = null,
    val origin: DataOrigin = DataOrigin.LOCAL,
    val createdAt: Long,
    val lastConfirmedAt: Long? = null
) {
    init {
        require(id.isNotBlank()) { "O id do checkpoint não pode ser vazio" }
        require(projectId.isNotBlank()) { "O checkpoint deve estar associado a um projectId existente" }
        require(summary.isNotBlank()) { "O resumo do checkpoint não pode ser vazio" }
    }
}
