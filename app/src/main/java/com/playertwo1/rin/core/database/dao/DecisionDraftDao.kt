package com.playertwo1.rin.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.playertwo1.rin.core.database.entity.DecisionDraftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DecisionDraftDao {
    @Query("SELECT * FROM decision_drafts WHERE projectId = :projectId ORDER BY updatedAt DESC")
    fun observeDecisionsForProject(projectId: String): Flow<List<DecisionDraftEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDecisionDraft(draft: DecisionDraftEntity)

    @Query("DELETE FROM decision_drafts WHERE id = :id")
    suspend fun deleteDecisionDraftById(id: String)
}
