package com.playertwo1.rin.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.playertwo1.rin.core.database.entity.WorkstationCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkstationCacheDao {
    @Query("SELECT * FROM workstation_cache WHERE workstationId = :workstationId")
    fun observeWorkstation(workstationId: String): Flow<WorkstationCacheEntity?>

    @Query("SELECT * FROM workstation_cache WHERE workstationId = :workstationId")
    suspend fun getWorkstation(workstationId: String): WorkstationCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWorkstation(workstation: WorkstationCacheEntity)
}
