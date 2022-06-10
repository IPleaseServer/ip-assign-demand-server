package site.iplease.iadserver.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.data.dto.DemandDto
import site.iplease.iadserver.data.entity.Demand
import site.iplease.iadserver.data.message.IpAssignDemandCreateMessage
import site.iplease.iadserver.data.request.AssignIpDemandRequest

interface DemandDataConverter {
    fun toDto(accountId: Long, request: AssignIpDemandRequest): Mono<DemandDto>
    fun toDto(entity: Demand): Mono<DemandDto>
    fun toEntity(dto: DemandDto): Mono<Demand>
    fun toIpAssignDemandCreateMessage(demand: DemandDto): Mono<IpAssignDemandCreateMessage>
}
