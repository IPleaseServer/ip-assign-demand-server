package site.iplease.iadserver.global.accept.util

import reactor.core.publisher.Mono

interface AssignIpValidator {
    fun validate(assignIp: String): Mono<Unit>
}