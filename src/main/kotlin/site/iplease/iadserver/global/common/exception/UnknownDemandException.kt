package site.iplease.iadserver.global.common.exception

import site.iplease.iadserver.global.error.IpleaseError

class UnknownDemandException(private val errorDetail: String): RuntimeException("$ERROR_MESSAGE - $errorDetail"),
    IpleaseError {
    companion object { private const val ERROR_MESSAGE = "존재하지 않는 신청입니다!" }

    override fun getErrorMessage() = ERROR_MESSAGE
    override fun getErrorDetail() = errorDetail
}