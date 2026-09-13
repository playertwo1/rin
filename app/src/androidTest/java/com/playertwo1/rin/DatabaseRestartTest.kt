package com.playertwo1.rin

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.playertwo1.rin.core.database.RinDatabase
import com.playertwo1.rin.core.database.repository.RoomProjectRepository
import com.playertwo1.rin.core.model.DataOrigin
import com.playertwo1.rin.core.model.LocalCheckpoint
import com.playertwo1.rin.core.model.LocalProject
import com.playertwo1.rin.core.model.ProjectStatus
import com.playertwo1.rin.core.model.SyncState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class DatabaseRestartTest {

    @Test
    fun database_persists_data_across_close_and_reopen_on_disk() {
        runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbFile = File(context.filesDir, "test_restart_database.db")
        if (dbFile.exists()) {
            dbFile.delete()
        }

        val testProjectId = UUID.randomUUID().toString()
        val testCheckpointId = UUID.randomUUID().toString()
        val timestamp = 1726230000000L

        // 1. Abrir primeira instância de conexão no disco
        val dbInstance1 = Room.databaseBuilder(context, RinDatabase::class.java, dbFile.absolutePath).build()
        val repo1 = RoomProjectRepository(dbInstance1)

        val project = LocalProject(
            id = testProjectId,
            workstationId = "workstation-lan-persist-test",
            remoteProjectId = "remote-proj-01",
            origin = DataOrigin.WORKSTATION_REMOTE,
            name = "Projeto Persistência Sobrevivente",
            description = "Deve sobreviver ao fechamento da conexão e reabertura",
            businessStatus = ProjectStatus.ACTIVE,
            syncState = SyncState.SYNCED,
            createdAt = timestamp,
            updatedAt = timestamp,
            lastConfirmedAt = timestamp
        )

        val checkpoint = LocalCheckpoint(
            id = testCheckpointId,
            projectId = testProjectId,
            title = "Checkpoint de Verificação",
            summary = "Criado antes do fechamento",
            createdAt = timestamp,
            lastConfirmedAt = timestamp
        )

        repo1.saveProject(project)
        repo1.saveCheckpoint(checkpoint)

        // 2. Fechar formalmente a primeira instância do banco de dados (simulando morte do processo)
        dbInstance1.close()

        // 3. Abrir uma SEGUNDA instância de banco de dados no mesmo arquivo físico no disco
        val dbInstance2 = Room.databaseBuilder(context, RinDatabase::class.java, dbFile.absolutePath).build()
        val repo2 = RoomProjectRepository(dbInstance2)

        // 4. Ler e comprovar recuperação idêntica dos dados e relacionamentos
        val recoveredProject = repo2.getProject(testProjectId)
        assertNotNull("O projeto deve sobreviver ao fechamento e ser lido na nova instância", recoveredProject)
        assertEquals(testProjectId, recoveredProject?.id)
        assertEquals("workstation-lan-persist-test", recoveredProject?.workstationId)
        assertEquals("remote-proj-01", recoveredProject?.remoteProjectId)
        assertEquals("Projeto Persistência Sobrevivente", recoveredProject?.name)
        assertEquals(timestamp, recoveredProject?.lastConfirmedAt)

        val recoveredCheckpoint = repo2.getLatestCheckpoint(testProjectId)
        assertNotNull("O checkpoint deve sobreviver ao fechamento", recoveredCheckpoint)
        assertEquals(testCheckpointId, recoveredCheckpoint?.id)
        assertEquals(testProjectId, recoveredCheckpoint?.projectId)
        assertEquals("Checkpoint de Verificação", recoveredCheckpoint?.title)
        assertEquals("Criado antes do fechamento", recoveredCheckpoint?.summary)

        // Fechar e limpar
        dbInstance2.close()
        dbFile.delete()
        }
    }
}
