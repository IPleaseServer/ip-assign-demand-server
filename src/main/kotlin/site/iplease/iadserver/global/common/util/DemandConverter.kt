package site.iplease.iadserver.global.common.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.common.data.entity.Demand
import site.iplease.iadserver.domain.common.data.request.AssignIpDemandRequest
import site.iplease.iadserver.global.cancel.data.dto.DemandCancelErrorOnStatusDto
import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptMessage
import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptedMessage
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.data.message.IpAssignDemandCancelMessage
import site.iplease.iadserver.global.common.data.message.IpAssignDemandCreateMessage
import site.iplease.iadserver.global.confirm.data.message.IpAssignDemandConfirmMessage
import site.iplease.iadserver.global.accept.data.dto.DemandAcceptedErrorOnManageDto

interface DemandConverter {
    fun toDto(demandId: Long): Mono<DemandDto>
    fun toDto(entity: Demand): Mono<DemandDto>
    fun toDto(accountId: Long, request: AssignIpDemandRequest): Mono<DemandDto>
    fun toDto(message: IpAssignDemandConfirmMessage): Mono<DemandDto>
    fun toEntity(dto: DemandDto): Mono<Demand>
    fun toEntity(dto: DemandCancelErrorOnStatusDto): Mono<Demand>
    fun toEntity(demand: DemandAcceptedErrorOnManageDto): Mono<Demand>
    fun toIpAssignDemandCreateMessage(demand: DemandDto): Mono<IpAssignDemandCreateMessage>
    fun toIpAssignDemandCancelMessage(demand: DemandDto): Mono<IpAssignDemandCancelMessage>
    fun toAssignIpCreateMessage(demand: DemandDto, message: IpAssignDemandAcceptMessage): Mono<IpAssignDemandAcceptedMessage>
}