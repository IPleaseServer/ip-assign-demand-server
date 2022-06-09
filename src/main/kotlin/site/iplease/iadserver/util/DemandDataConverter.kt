package site.iplease.iadserver.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.data.dto.DemandDto
import site.iplease.iadserver.data.request.AssignIpDemandRequest

interface DemandDataConverter {
    fun toDto(accountId: Long, request: AssignIpDemandRequest): Mono<DemandDto>
}
