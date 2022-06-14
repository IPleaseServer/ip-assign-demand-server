package site.iplease.iadserver.domain.demand.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.data.entity.Demand
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandCreateMessage
import site.iplease.iadserver.domain.demand.data.request.AssignIpDemandRequest
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandCancelMessage

interface DemandDataConverter {
    fun toDto(accountId: Long, request: AssignIpDemandRequest): Mono<DemandDto>
    fun toDto(entity: Demand): Mono<DemandDto>
    fun toEntity(dto: DemandDto): Mono<Demand>
    fun toIpAssignDemandCreateMessage(demand: DemandDto): Mono<IpAssignDemandCreateMessage>
    fun toIpAssignDemandCancelMessage(demand: DemandDto): Mono<IpAssignDemandCancelMessage>
}