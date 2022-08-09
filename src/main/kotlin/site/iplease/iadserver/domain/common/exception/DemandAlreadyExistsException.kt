package site.iplease.iadserver.domain.common.exception

import site.iplease.iadserver.global.error.IpleaseError

class DemandAlreadyExistsException(private val errorDetail: String): RuntimeException("$ERROR_MESSAGE - $errorDetail"),
    IpleaseError {
    companion object { private const val ERROR_MESSAGE = "이미 존재하는 신청입니다!" }

    override fun getErrorMessage() = ERROR_MESSAGE
    override fun getErrorDetail() = errorDetail
}