package com.playertwo1.rin.ui.screens.projects.checkpoints

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.playertwo1.rin.core.database.repository.ProjectRepository
import com.playertwo1.rin.core.model.DataOrigin
import com.playertwo1.rin.core.model.LocalCheckpoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class CheckpointFormUiState(
    val isOpen: Boolean = false,
    val isEditing: Boolean = false,
    val checkpointId: String? = null,
    val projectId: String = "",
    val title: String = "",
    val summary: String = "",
    val nextStep: String = "",
    val blockers: String = "",
    val referencesText: String = "",
    val summaryError: String? = null,
    val isSaving: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class CheckpointFormViewModel(
    private val repository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckpointFormUiState())
    val uiState: StateFlow<CheckpointFormUiState> = _uiState.asStateFlow()

    fun openForCreate(projectId: String) {
        _uiState.value = CheckpointFormUiState(
            isOpen = true,
            isEditing = false,
            projectId = projectId,
            title = "Checkpoint"
        )
    }

    fun openForEdit(checkpoint: LocalCheckpoint) {
        _uiState.value = CheckpointFormUiState(
            isOpen = true,
            isEditing = true,
            checkpointId = checkpoint.id,
            projectId = checkpoint.projectId,
            title = checkpoint.title,
            summary = checkpoint.summary,
            nextStep = checkpoint.nextStep ?: "",
            blockers = checkpoint.blockers ?: "",
            referencesText = checkpoint.referencesText ?: ""
        )
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateSummary(summary: String) {
        _uiState.value = _uiState.value.copy(
            summary = summary,
            summaryError = if (summary.isNotBlank()) null else _uiState.value.summaryError
        )
    }

    fun updateNextStep(nextStep: String) {
        _uiState.value = _uiState.value.copy(nextStep = nextStep)
    }

    fun updateBlockers(blockers: String) {
        _uiState.value = _uiState.value.copy(blockers = blockers)
    }

    fun updateReferencesText(referencesText: String) {
        _uiState.value = _uiState.value.copy(referencesText = referencesText)
    }

    fun dismiss() {
        _uiState.value = CheckpointFormUiState(isOpen = false)
    }

    fun save(onSuccess: (() -> Unit)? = null) {
        val currentState = _uiState.value
        val trimmedSummary = currentState.summary.trim()
        if (trimmedSummary.isBlank()) {
            _uiState.value = currentState.copy(summaryError = "O resumo factual é obrigatório")
            return
        }

        val resolvedTitle = currentState.title.trim().ifEmpty { "Checkpoint" }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, errorMessage = null)
            try {
                val now = System.currentTimeMillis()
                if (currentState.isEditing && currentState.checkpointId != null) {
                    val existing = repository.getCheckpoint(currentState.checkpointId)
                    if (existing != null) {
                        val updatedCheckpoint = existing.copy(
                            title = resolvedTitle,
                            summary = trimmedSummary,
                            nextStep = currentState.nextStep.trim().ifEmpty { null },
                            blockers = currentState.blockers.trim().ifEmpty { null },
                            referencesText = currentState.referencesText.trim().ifEmpty { null }
                        )
                        repository.saveCheckpoint(updatedCheckpoint)
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isSaving = false,
                            errorMessage = "Checkpoint não encontrado para edição"
                        )
                        return@launch
                    }
                } else {
                    val newCheckpoint = LocalCheckpoint(
                        id = UUID.randomUUID().toString(),
                        projectId = currentState.projectId,
                        title = resolvedTitle,
                        summary = trimmedSummary,
                        nextStep = currentState.nextStep.trim().ifEmpty { null },
                        blockers = currentState.blockers.trim().ifEmpty { null },
                        referencesText = currentState.referencesText.trim().ifEmpty { null },
                        origin = DataOrigin.LOCAL,
                        createdAt = now,
                        lastConfirmedAt = null
                    )
                    repository.saveCheckpoint(newCheckpoint)
                }

                val project = repository.getProject(currentState.projectId)
                if (project != null) {
                    repository.saveProject(project.copy(updatedAt = now))
                }

                _uiState.value = CheckpointFormUiState(isOpen = false, isSuccess = true)
                onSuccess?.invoke()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = "Erro ao salvar checkpoint: ${e.message}"
                )
            }
        }
    }
}

class CheckpointFormViewModelFactory(
    private val repository: ProjectRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckpointFormViewModel::class.java)) {
            return CheckpointFormViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
