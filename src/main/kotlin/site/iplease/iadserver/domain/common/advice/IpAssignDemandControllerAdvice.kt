package site.iplease.iadserver.domain.common.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.common.controller.IpAssignDemandController
import site.iplease.iadserver.global.common.exception.DemandNotExistException
import site.iplease.iadserver.domain.common.exception.NotOwnedDemandException
import site.iplease.iadserver.domain.common.exception.WrongExpireDateException
import site.iplease.iadserver.domain.common.exception.WrongTitleException

@RestControllerAdvice(basePackageClasses = [IpAssignDemandController::class])
class IpAssignDemandControllerAdvice {
    @ExceptionHandler(WrongExpireDateException::class)
    fun handle(exception: WrongExpireDateException): Mono<ResponseEntity<String>> =
        ResponseEntity.badRequest()
            .body(exception.localizedMessage)
            .toMono()

    @ExceptionHandler(WrongTitleException::class)
    fun handle(exception: WrongTitleException): Mono<ResponseEntity<String>> =
        ResponseEntity.badRequest()
            .body(exception.localizedMessage)
            .toMono()

    @ExceptionHandler(NotOwnedDemandException::class)
    fun handle(exception: NotOwnedDemandException): Mono<ResponseEntity<String>> =
        ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(exception.localizedMessage)
            .toMono()

    @ExceptionHandler(DemandNotExistException::class)
    fun handle(exception: DemandNotExistException): Mono<ResponseEntity<String>> =
        ResponseEntity.badRequest()
            .body(exception.localizedMessage)
            .toMono()
}