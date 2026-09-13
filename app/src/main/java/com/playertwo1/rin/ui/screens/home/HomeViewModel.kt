package com.playertwo1.rin.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.playertwo1.rin.core.database.repository.ProjectRepository
import com.playertwo1.rin.core.model.DataOrigin
import com.playertwo1.rin.core.model.LocalCheckpoint
import com.playertwo1.rin.core.model.LocalProject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class HomeProjectItem(
    val project: LocalProject,
    val latestCheckpoint: LocalCheckpoint?
)

data class HomeUiState(
    val activeProjectItem: HomeProjectItem? = null,
    val recentProjects: List<HomeProjectItem> = emptyList(),
    val localProjectsCount: Int = 0,
    val cachedProjectsCount: Int = 0,
    val workstationStatusLabel: String = "Conectada (Simulada)",
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val repository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            repository.observeProjects().collectLatest { projects ->
                val localCount = projects.count { it.origin == DataOrigin.LOCAL }
                val cachedCount = projects.count { it.origin == DataOrigin.WORKSTATION_REMOTE }

                val items = projects.map { project ->
                    val checkpoint = repository.getLatestCheckpoint(project.id)
                    HomeProjectItem(project = project, latestCheckpoint = checkpoint)
                }

                // O projeto ativo em destaque é o com atividade mais recente
                val active = items.firstOrNull()

                _uiState.value = _uiState.value.copy(
                    activeProjectItem = active,
                    recentProjects = items,
                    localProjectsCount = localCount,
                    cachedProjectsCount = cachedCount,
                    isLoading = false
                )
            }
        }
    }
}

class HomeViewModelFactory(
    private val repository: ProjectRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
