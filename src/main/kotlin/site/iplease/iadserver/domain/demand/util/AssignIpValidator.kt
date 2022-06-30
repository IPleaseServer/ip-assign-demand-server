package site.iplease.iadserver.domain.demand.util

import reactor.core.publisher.Mono

interface AssignIpValidator {
    fun validate(assignIp: String): Mono<Unit>
}