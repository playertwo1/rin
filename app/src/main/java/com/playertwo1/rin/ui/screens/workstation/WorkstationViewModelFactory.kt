package com.playertwo1.rin.ui.screens.workstation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.playertwo1.rin.core.network.WorkstationGateway

class WorkstationViewModelFactory(
    private val gateway: WorkstationGateway
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkstationViewModel::class.java)) {
            return WorkstationViewModel(gateway) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
