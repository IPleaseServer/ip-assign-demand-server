package site.iplease.iadserver.domain.common.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.common.controller.IpAssignDemandController
import site.iplease.iadserver.global.common.exception.UnknownDemandException
import site.iplease.iadserver.domain.common.exception.NotOwnedDemandException
import site.iplease.iadserver.domain.common.exception.WrongExpireDateException
import site.iplease.iadserver.domain.common.exception.WrongTitleException
import site.iplease.iadserver.global.error.ErrorResponse
import site.iplease.iadserver.global.error.ErrorStatus

@RestControllerAdvice(basePackageClasses = [IpAssignDemandController::class])
class IpAssignDemandControllerAdvice {
    @ExceptionHandler(WrongExpireDateException::class)
    fun handle(e: WrongExpireDateException): Mono<ResponseEntity<ErrorResponse>> =
        ResponseEntity.badRequest()
            .body(ErrorResponse(
                status = ErrorStatus.WRONG_EXPIRE_DATE,
                message = e.getErrorMessage(),
                detail = e.getErrorDetail()
            )).toMono()

    @ExceptionHandler(WrongTitleException::class)
    fun handle(e: WrongTitleException): Mono<ResponseEntity<ErrorResponse>> =
        ResponseEntity.badRequest()
            .body(ErrorResponse(
                status = ErrorStatus.WRONG_TITLE,
                message = e.getErrorMessage(),
                detail = e.getErrorDetail()
            )).toMono()

    @ExceptionHandler(NotOwnedDemandException::class)
    fun handle(e: NotOwnedDemandException): Mono<ResponseEntity<ErrorResponse>> =
        ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ErrorResponse(
                status = ErrorStatus.NOT_OWNED_DEMAND,
                message = e.getErrorMessage(),
                detail = e.getErrorDetail()
            )).toMono()

    @ExceptionHandler(UnknownDemandException::class)
    fun handle(e: UnknownDemandException): Mono<ResponseEntity<ErrorResponse>> =
        ResponseEntity.badRequest()
            .body(ErrorResponse(
                status = ErrorStatus.UNKNOWN_DEMAND,
                message = e.getErrorMessage(),
                detail = e.getErrorDetail()
            )
            ).toMono()
}