package com.playertwo1.rin

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.playertwo1.rin.core.database.RinDatabase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class DatabaseMigrationTest {

    @Test
    fun migration_1_to_2_adds_priority_column_preserving_existing_data() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbFile = File(context.filesDir, "test_migration_1_2.db")
        if (dbFile.exists()) {
            dbFile.delete()
        }

        // 1. Criar banco com o schema exato da versão 1 (sem coluna priority)
        val helperConfig = androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration.builder(context)
            .name(dbFile.absolutePath)
            .callback(object : androidx.sqlite.db.SupportSQLiteOpenHelper.Callback(1) {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    db.execSQL(
                        "CREATE TABLE IF NOT EXISTS projects (" +
                            "id TEXT NOT NULL, " +
                            "workstationId TEXT, " +
                            "remoteProjectId TEXT, " +
                            "origin TEXT NOT NULL, " +
                            "name TEXT NOT NULL, " +
                            "description TEXT NOT NULL, " +
                            "currentBranch TEXT, " +
                            "gitCommitHash TEXT, " +
                            "businessStatus TEXT NOT NULL, " +
                            "syncState TEXT NOT NULL, " +
                            "quotaUsagePercent REAL, " +
                            "testRunStatus TEXT, " +
                            "createdAt INTEGER NOT NULL, " +
                            "updatedAt INTEGER NOT NULL, " +
                            "lastConfirmedAt INTEGER, " +
                            "PRIMARY KEY(id))"
                    )
                }
                override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {}
            })
            .build()

        val helper = FrameworkSQLiteOpenHelperFactory().create(helperConfig)
        val v1Db = helper.writableDatabase

        // Inserir registro na v1
        v1Db.execSQL(
            "INSERT INTO projects (id, origin, name, description, businessStatus, syncState, createdAt, updatedAt) " +
                "VALUES ('proj-v1-legacy', 'LOCAL', 'Projeto Histórico V1', 'Criado antes da v2', 'ACTIVE', 'LOCAL_ONLY', 1000, 1000)"
        )

        // 2. Executar migração oficial MIGRATION_1_2
        RinDatabase.MIGRATION_1_2.migrate(v1Db)

        // 3. Validar que a coluna 'priority' foi adicionada e preenchida com o padrão 'NORMAL'
        val cursor = v1Db.query("SELECT id, name, priority FROM projects WHERE id = 'proj-v1-legacy'")
        assertTrue(cursor.moveToFirst())
        assertEquals("proj-v1-legacy", cursor.getString(0))
        assertEquals("Projeto Histórico V1", cursor.getString(1))
        assertEquals("NORMAL", cursor.getString(2))
        cursor.close()

        v1Db.close()
        dbFile.delete()
    }
}
