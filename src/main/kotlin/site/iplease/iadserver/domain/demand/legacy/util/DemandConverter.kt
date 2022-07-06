package site.iplease.iadserver.domain.demand.legacy.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.legacy.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.legacy.data.dto.DemandCancelErrorOnStatusDto
import site.iplease.iadserver.domain.demand.legacy.data.entity.Demand
import site.iplease.iadserver.domain.demand.legacy.data.request.AssignIpDemandRequest
import site.iplease.iadserver.global.demand.data.message.*

interface DemandConverter {
    fun toDto(demandId: Long): Mono<DemandDto>
    fun toDto(entity: Demand): Mono<DemandDto>
    fun toDto(accountId: Long, request: AssignIpDemandRequest): Mono<DemandDto>
    fun toDto(message: IpAssignDemandConfirmMessage): Mono<DemandDto>
    fun toEntity(dto: DemandDto): Mono<Demand>
    fun toEntity(dto: DemandCancelErrorOnStatusDto): Mono<Demand>
    fun toIpAssignDemandCreateMessage(demand: DemandDto): Mono<IpAssignDemandCreateMessage>
    fun toIpAssignDemandCancelMessage(demand: DemandDto): Mono<IpAssignDemandCancelMessage>
    fun toAssignIpCreateMessage(demand: DemandDto, message: IpAssignDemandAcceptMessage): Mono<IpAssignDemandAcceptedMessage>
}