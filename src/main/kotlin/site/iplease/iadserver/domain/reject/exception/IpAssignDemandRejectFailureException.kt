package site.iplease.iadserver.domain.reject.exception

import site.iplease.iadserver.global.error.IpleaseError

class IpAssignDemandRejectFailureException(private val throwable: Throwable): RuntimeException("$ERROR_MESSAGE - ${throwable.localizedMessage}"),
    IpleaseError {
    companion object { private const val ERROR_MESSAGE = "신청거절에 실패하였습니다!" }

    override fun getErrorMessage() = ERROR_MESSAGE
    override fun getErrorDetail() = throwable.localizedMessage!!
}