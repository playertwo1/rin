package com.playertwo1.rin.ui.screens.workstation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playertwo1.rin.core.model.NetworkResult
import com.playertwo1.rin.core.network.WorkstationGateway
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WorkstationUiState(
    val isConnected: Boolean = false,
    val isSimulated: Boolean = true,
    val workstationName: String? = null,
    val version: String? = null,
    val capabilities: List<String> = emptyList(),
    val statusDescription: String = "Carregando status da workstation...",
    val errorMessage: String? = null
)

class WorkstationViewModel(
    private val gateway: WorkstationGateway
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkstationUiState())
    val uiState: StateFlow<WorkstationUiState> = _uiState.asStateFlow()

    init {
        refreshHealth()
    }

    fun refreshHealth() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(statusDescription = "Consultando gateway...")
            when (val result = gateway.getHealth()) {
                is NetworkResult.Success -> {
                    val health = result.data
                    _uiState.value = WorkstationUiState(
                        isConnected = health.isHealthy,
                        isSimulated = true,
                        workstationName = health.workstationName,
                        version = health.version,
                        capabilities = health.capabilities.toList().sorted(),
                        statusDescription = if (health.isHealthy) "Conectado via Gateway Simulado" else "Serviço com falha de saúde",
                        errorMessage = null
                    )
                }
                is NetworkResult.Error -> {
                    _uiState.value = WorkstationUiState(
                        isConnected = false,
                        isSimulated = true,
                        statusDescription = "Desconectado",
                        errorMessage = "${result.error.code}: ${result.error.message}"
                    )
                }
            }
        }
    }
}
