package com.playertwo1.rin.core.model

enum class SyncState {
    LOCAL_ONLY,
    SYNCED,
    PENDING_SYNC,
    STALE,
    CONFLICT
}
