package com.playertwo1.rin.ui.screens.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.playertwo1.rin.core.database.repository.ProjectRepository
import com.playertwo1.rin.core.model.DataOrigin
import com.playertwo1.rin.core.model.LocalProject
import com.playertwo1.rin.core.model.ProjectPriority
import com.playertwo1.rin.core.model.ProjectStatus
import com.playertwo1.rin.core.model.SyncState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class ProjectFormUiState(
    val isOpen: Boolean = false,
    val isEditing: Boolean = false,
    val projectId: String? = null,
    val name: String = "",
    val description: String = "",
    val priority: ProjectPriority = ProjectPriority.NORMAL,
    val status: ProjectStatus = ProjectStatus.ACTIVE,
    val nameError: String? = null,
    val isSaving: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class ProjectFormViewModel(
    private val repository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectFormUiState())
    val uiState: StateFlow<ProjectFormUiState> = _uiState.asStateFlow()

    fun openForCreate() {
        _uiState.value = ProjectFormUiState(
            isOpen = true,
            isEditing = false,
            priority = ProjectPriority.NORMAL,
            status = ProjectStatus.ACTIVE
        )
    }

    fun openForEdit(project: LocalProject) {
        _uiState.value = ProjectFormUiState(
            isOpen = true,
            isEditing = true,
            projectId = project.id,
            name = project.name,
            description = project.description,
            priority = project.priority,
            status = project.businessStatus
        )
    }

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            nameError = if (name.isNotBlank()) null else _uiState.value.nameError
        )
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun updatePriority(priority: ProjectPriority) {
        _uiState.value = _uiState.value.copy(priority = priority)
    }

    fun updateStatus(status: ProjectStatus) {
        _uiState.value = _uiState.value.copy(status = status)
    }

    fun dismiss() {
        _uiState.value = ProjectFormUiState(isOpen = false)
    }

    fun save(onSuccess: (() -> Unit)? = null) {
        val currentState = _uiState.value
        val trimmedName = currentState.name.trim()
        if (trimmedName.isBlank()) {
            _uiState.value = currentState.copy(nameError = "O nome do projeto é obrigatório")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, errorMessage = null)
            try {
                val now = System.currentTimeMillis()
                if (currentState.isEditing && currentState.projectId != null) {
                    val existing = repository.getProject(currentState.projectId)
                    if (existing != null) {
                        val updatedProject = existing.copy(
                            name = trimmedName,
                            description = currentState.description.trim(),
                            priority = currentState.priority,
                            businessStatus = currentState.status,
                            updatedAt = now
                        )
                        repository.saveProject(updatedProject)
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isSaving = false,
                            errorMessage = "Projeto não encontrado para edição"
                        )
                        return@launch
                    }
                } else {
                    val newProject = LocalProject(
                        id = UUID.randomUUID().toString(),
                        workstationId = null,
                        remoteProjectId = null,
                        origin = DataOrigin.LOCAL,
                        name = trimmedName,
                        description = currentState.description.trim(),
                        currentBranch = null,
                        gitCommitHash = null,
                        businessStatus = currentState.status,
                        priority = currentState.priority,
                        syncState = SyncState.LOCAL_ONLY,
                        quotaUsagePercent = null,
                        testRunStatus = null,
                        createdAt = now,
                        updatedAt = now,
                        lastConfirmedAt = null
                    )
                    repository.saveProject(newProject)
                }
                _uiState.value = ProjectFormUiState(isOpen = false, isSuccess = true)
                onSuccess?.invoke()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = "Erro ao salvar projeto: ${e.message}"
                )
            }
        }
    }
}

class ProjectFormViewModelFactory(
    private val repository: ProjectRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectFormViewModel::class.java)) {
            return ProjectFormViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
