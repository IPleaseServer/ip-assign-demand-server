package site.iplease.iadserver.global.error

interface IpleaseError {
    fun getErrorMessage(): String
    fun getErrorDetail(): String
}