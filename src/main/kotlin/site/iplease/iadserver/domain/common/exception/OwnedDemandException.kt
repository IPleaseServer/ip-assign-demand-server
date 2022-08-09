package site.iplease.iadserver.domain.common.exception

import site.iplease.iadserver.global.error.IpleaseError

class OwnedDemandException(private val errorDetail: String): RuntimeException("$ERROR_MESSAGE - $errorDetail"),
    IpleaseError {
    companion object { private const val ERROR_MESSAGE = "본인이 소유한 신청입니다!" }

    override fun getErrorMessage() = ERROR_MESSAGE
    override fun getErrorDetail() = errorDetail
}