package com.playertwo1.rin.core.model

data class LocalDecisionDraft(
    val id: String,
    val projectId: String,
    val question: String,
    val options: List<String>,
    val selectedOption: String? = null,
    val isConfirmedLocally: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long
) {
    init {
        require(id.isNotBlank()) { "O id da decisão não pode ser vazio" }
        require(projectId.isNotBlank()) { "A decisão deve estar associada a um projectId existente" }
        require(question.isNotBlank()) { "A pergunta da decisão não pode ser vazia" }
        require(options.isNotEmpty()) { "A decisão deve conter opções válidas" }
    }
}
