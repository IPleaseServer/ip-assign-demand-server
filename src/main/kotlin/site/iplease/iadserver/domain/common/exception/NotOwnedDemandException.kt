package site.iplease.iadserver.domain.common.exception

import site.iplease.iadserver.global.error.IpleaseError

class NotOwnedDemandException(private val errorDetail: String): RuntimeException("$ERROR_MESSAGE - $errorDetail"),
    IpleaseError {
    companion object { private const val ERROR_MESSAGE = "해당 계정이 소유한 신청이 아닙니다!" }

    override fun getErrorMessage() = ERROR_MESSAGE
    override fun getErrorDetail() = errorDetail
}