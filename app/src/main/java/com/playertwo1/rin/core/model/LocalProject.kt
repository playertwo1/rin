package com.playertwo1.rin.core.model

data class LocalProject(
    val id: String,
    val workstationId: String? = null,
    val remoteProjectId: String? = null,
    val origin: DataOrigin,
    val name: String,
    val description: String,
    val currentBranch: String? = null,
    val gitCommitHash: String? = null,
    val businessStatus: ProjectStatus = ProjectStatus.ACTIVE,
    val priority: ProjectPriority = ProjectPriority.NORMAL,
    val syncState: SyncState = if (origin == DataOrigin.LOCAL) SyncState.LOCAL_ONLY else SyncState.SYNCED,
    val quotaUsagePercent: Float? = null,
    val testRunStatus: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val lastConfirmedAt: Long? = null
) {
    init {
        require(id.isNotBlank()) { "O identificador local do projeto não pode ser vazio" }
        require(name.isNotBlank()) { "O nome do projeto não pode ser vazio" }
        if (origin == DataOrigin.WORKSTATION_REMOTE) {
            require(!workstationId.isNullOrBlank()) { "workstationId é obrigatório para projeto de origem remota" }
            require(!remoteProjectId.isNullOrBlank()) { "remoteProjectId é obrigatório para projeto de origem remota" }
        }
    }

    val remoteCompositeKey: String?
        get() = if (workstationId != null && remoteProjectId != null) {
            "$workstationId:$remoteProjectId"
        } else {
            null
        }
}
