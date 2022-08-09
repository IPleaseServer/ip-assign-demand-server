package site.iplease.iadserver.global.error

data class ErrorResponse (
    val status: ErrorStatus,
    val message: String,
    val detail: String
)