package site.iplease.iadserver.domain.common.exception

import site.iplease.iadserver.global.error.IpleaseError

class WrongExpireDateException(private val errorDetail: String): RuntimeException("$ERROR_MESSAGE - $errorDetail"),
    IpleaseError {
    companion object { private const val ERROR_MESSAGE = "잘못된 만료일입니다!" }

    override fun getErrorMessage() = ERROR_MESSAGE
    override fun getErrorDetail() = errorDetail
}