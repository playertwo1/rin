package com.playertwo1.rin.core.model

sealed interface NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>
    data class Error(val error: WorkstationError) : NetworkResult<Nothing>
}

data class WorkstationError(
    val code: String,
    val message: String,
    val category: ErrorCategory,
    val details: String? = null
)

enum class ErrorCategory {
    NETWORK_UNAVAILABLE,
    TIMEOUT,
    NOT_FOUND,
    UNAUTHORIZED,
    FORBIDDEN,
    SERVER_ERROR,
    INCOMPATIBLE_VERSION,
    CAPABILITY_MISSING,
    DESERIALIZATION,
    UNKNOWN
}
