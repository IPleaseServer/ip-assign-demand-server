package site.iplease.iadserver.domain.reject.exception

import site.iplease.iadserver.global.error.IpleaseError

class AlreadyRejectedDemandException(private val errorDetail: String): RuntimeException("$ERROR_MESSAGE - $errorDetail"),
    IpleaseError {
    companion object { private const val ERROR_MESSAGE = "이미 거절된 신청입니다!" }

    override fun getErrorMessage() = ERROR_MESSAGE
    override fun getErrorDetail() = errorDetail
}