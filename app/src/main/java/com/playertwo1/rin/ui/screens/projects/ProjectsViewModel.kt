package com.playertwo1.rin.ui.screens.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.playertwo1.rin.core.database.repository.ProjectRepository
import com.playertwo1.rin.core.model.LocalCheckpoint
import com.playertwo1.rin.core.model.LocalProject
import com.playertwo1.rin.core.sync.WorkstationSyncManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class ProjectsUiState(
    val projects: List<LocalProject> = emptyList(),
    val selectedProject: LocalProject? = null,
    val selectedProjectCheckpoint: LocalCheckpoint? = null,
    val selectedProjectCheckpoints: List<LocalCheckpoint> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isSimulated: Boolean = true,
    val errorMessage: String? = null
)

class ProjectsViewModel(
    private val repository: ProjectRepository,
    private val syncManager: WorkstationSyncManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectsUiState(isLoading = true))
    val uiState: StateFlow<ProjectsUiState> = _uiState.asStateFlow()

    private var checkpointsJob: kotlinx.coroutines.Job? = null

    init {
        // Observar Room como Fonte Única da Verdade (Single Source of Truth)
        viewModelScope.launch {
            repository.observeProjects().collectLatest { localProjects ->
                _uiState.value = _uiState.value.copy(
                    projects = localProjects,
                    isLoading = false
                )
            }
        }
        // Iniciar sincronização com o gateway (fake ou real)
        loadProjects()
    }

    fun loadProjects() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, errorMessage = null)
            val result = syncManager.syncProjects()
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    errorMessage = null
                )
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Erro desconhecido na sincronização"
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    errorMessage = "Modo Offline / Falha: exibindo cache local preservado ($errorMsg)"
                )
            }
        }
    }

    fun selectProject(projectId: String) {
        checkpointsJob?.cancel()
        checkpointsJob = viewModelScope.launch {
            repository.observeCheckpoints(projectId).collectLatest { list ->
                _uiState.value = _uiState.value.copy(
                    selectedProjectCheckpoints = list,
                    selectedProjectCheckpoint = list.firstOrNull()
                )
            }
        }

        viewModelScope.launch {
            val project = repository.getProject(projectId)
            val checkpoint = repository.getLatestCheckpoint(projectId)
            _uiState.value = _uiState.value.copy(
                selectedProject = project,
                selectedProjectCheckpoint = checkpoint
            )
            // Tentar sincronizar detalhes adicionais se for projeto remoto
            if (project != null) {
                val detailResult = syncManager.syncProjectDetail(projectId)
                if (detailResult.isSuccess) {
                    val updated = repository.getProject(projectId)
                    val updatedCk = repository.getLatestCheckpoint(projectId)
                    _uiState.value = _uiState.value.copy(
                        selectedProject = updated,
                        selectedProjectCheckpoint = updatedCk
                    )
                }
            }
        }
    }

    fun clearSelectedProject() {
        checkpointsJob?.cancel()
        checkpointsJob = null
        _uiState.value = _uiState.value.copy(
            selectedProject = null,
            selectedProjectCheckpoint = null,
            selectedProjectCheckpoints = emptyList()
        )
    }

    fun refreshSelectedProject() {
        val currentId = _uiState.value.selectedProject?.id ?: return
        viewModelScope.launch {
            val project = repository.getProject(currentId)
            val checkpoint = repository.getLatestCheckpoint(currentId)
            _uiState.value = _uiState.value.copy(
                selectedProject = project,
                selectedProjectCheckpoint = checkpoint
            )
        }
    }

    fun deleteCheckpointLocally(checkpointId: String) {
        viewModelScope.launch {
            repository.deleteCheckpoint(checkpointId)
        }
    }

    fun toggleArchive(projectId: String) {
        viewModelScope.launch {
            val project = repository.getProject(projectId) ?: return@launch
            val newStatus = if (project.businessStatus == com.playertwo1.rin.core.model.ProjectStatus.ARCHIVED) {
                com.playertwo1.rin.core.model.ProjectStatus.ACTIVE
            } else {
                com.playertwo1.rin.core.model.ProjectStatus.ARCHIVED
            }
            val updated = project.copy(
                businessStatus = newStatus,
                updatedAt = System.currentTimeMillis()
            )
            repository.saveProject(updated)
            val updatedCk = repository.getLatestCheckpoint(projectId)
            _uiState.value = _uiState.value.copy(
                selectedProject = updated,
                selectedProjectCheckpoint = updatedCk
            )
        }
    }

    fun deleteProjectLocally(projectId: String) {
        viewModelScope.launch {
            repository.deleteProject(projectId)
            _uiState.value = _uiState.value.copy(
                selectedProject = null,
                selectedProjectCheckpoint = null
            )
        }
    }
}

class ProjectsViewModelFactory(
    private val repository: ProjectRepository,
    private val syncManager: WorkstationSyncManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectsViewModel::class.java)) {
            return ProjectsViewModel(repository, syncManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
