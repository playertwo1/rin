package com.playertwo1.rin.core.di

import android.content.Context
import com.playertwo1.rin.core.database.RinDatabase
import com.playertwo1.rin.core.database.repository.ProjectRepository
import com.playertwo1.rin.core.database.repository.RoomProjectRepository
import com.playertwo1.rin.core.network.WorkstationGateway
import com.playertwo1.rin.core.network.fake.FakeWorkstationGateway

import com.playertwo1.rin.core.sync.WorkstationSyncManager

/**
 * Container de injeção de dependências manual para o app RIN.
 * Mantém instâncias de serviços, repositórios e gateways.
 */
interface AppContainer {
    val appContext: Context
    val workstationGateway: WorkstationGateway
    val database: RinDatabase
    val projectRepository: ProjectRepository
    val workstationSyncManager: WorkstationSyncManager
}

class DefaultAppContainer(
    override val appContext: Context,
    override val workstationGateway: WorkstationGateway = FakeWorkstationGateway(),
    override val database: RinDatabase = RinDatabase.createPersistent(appContext),
    override val projectRepository: ProjectRepository = RoomProjectRepository(database),
    override val workstationSyncManager: WorkstationSyncManager = WorkstationSyncManager(
        workstationGateway,
        projectRepository
    )
) : AppContainer
