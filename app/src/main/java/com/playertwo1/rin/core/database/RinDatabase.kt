package com.playertwo1.rin.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.playertwo1.rin.core.database.dao.CheckpointDao
import com.playertwo1.rin.core.database.dao.DecisionDraftDao
import com.playertwo1.rin.core.database.dao.ProjectDao
import com.playertwo1.rin.core.database.dao.WorkstationCacheDao
import com.playertwo1.rin.core.database.entity.CheckpointEntity
import com.playertwo1.rin.core.database.entity.DecisionDraftEntity
import com.playertwo1.rin.core.database.entity.ProjectEntity
import com.playertwo1.rin.core.database.entity.WorkstationCacheEntity

@Database(
    entities = [
        ProjectEntity::class,
        CheckpointEntity::class,
        DecisionDraftEntity::class,
        WorkstationCacheEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class RinDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun checkpointDao(): CheckpointDao
    abstract fun decisionDraftDao(): DecisionDraftDao
    abstract fun workstationCacheDao(): WorkstationCacheDao

    companion object {
        const val DATABASE_NAME = "rin_local.db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE projects ADD COLUMN priority TEXT NOT NULL DEFAULT 'NORMAL'")
            }
        }

        fun createInMemory(context: Context): RinDatabase {
            return Room.inMemoryDatabaseBuilder(context, RinDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        }

        fun createPersistent(context: Context): RinDatabase {
            // NOTA: fallbackToDestructiveMigration() é expressamente PROIBIDO pelas regras
            // de integridade e preservação de rascunhos do usuário (ADR 0003).
            return Room.databaseBuilder(
                context.applicationContext,
                RinDatabase::class.java,
                DATABASE_NAME
            )
            .addMigrations(MIGRATION_1_2)
            .build()
        }
    }
}
