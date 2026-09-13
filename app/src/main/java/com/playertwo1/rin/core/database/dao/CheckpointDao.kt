package com.playertwo1.rin.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.playertwo1.rin.core.database.entity.CheckpointEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckpointDao {
    @Query("SELECT * FROM checkpoints WHERE projectId = :projectId ORDER BY createdAt DESC")
    fun observeCheckpointsForProject(projectId: String): Flow<List<CheckpointEntity>>

    @Query("SELECT * FROM checkpoints WHERE projectId = :projectId ORDER BY createdAt DESC LIMIT 1")
    fun observeLatestCheckpointForProject(projectId: String): Flow<CheckpointEntity?>

    @Query("SELECT * FROM checkpoints WHERE projectId = :projectId ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestCheckpointForProject(projectId: String): CheckpointEntity?

    @Query("SELECT * FROM checkpoints WHERE id = :id")
    suspend fun getCheckpointById(id: String): CheckpointEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCheckpoint(checkpoint: CheckpointEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCheckpoints(checkpoints: List<CheckpointEntity>)

    @Query("DELETE FROM checkpoints WHERE id = :id")
    suspend fun deleteCheckpointById(id: String)
}
