package site.iplease.iadserver.domain.demand.util

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.data.dto.DemandCancelErrorOnStatusDto
import site.iplease.iadserver.domain.demand.data.entity.Demand
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandCreateMessage
import site.iplease.iadserver.domain.demand.data.request.AssignIpDemandRequest
import site.iplease.iadserver.domain.demand.data.type.AssignIpUsageType
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandCancelMessage
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandConfirmMessage
import java.time.LocalDate

@Component
class DemandConverterImpl: DemandConverter {
    override fun toDto(accountId: Long, request: AssignIpDemandRequest): Mono<DemandDto>  =
        Unit.toMono().map { DemandDto(
            issuerId = accountId,
            title = request.title,
            description = request.description,
            usage = request.usage,
            expireAt = request.expireAt
        ) }

    override fun toDto(entity: Demand): Mono<DemandDto> =
        entity.toMono().map { DemandDto(
                id = it.identifier,
                issuerId = it.issuerId,
                title = it.title,
                description = it.description,
                usage = it.usage,
                expireAt = it.expireAt
            ) }

    override fun toDto(message: IpAssignDemandConfirmMessage): Mono<DemandDto> =
        message.toMono().map { DemandDto(
            id = it.demandId,
            issuerId = 0,
            title = "",
            description = "",
            usage = AssignIpUsageType.USE_NETWORK,
            expireAt = LocalDate.MAX
        ) }

    override fun toEntity(dto: DemandDto): Mono<Demand> =
        dto.toMono().map { Demand(
                identifier = it.id,
                issuerId = it.issuerId,
                title = it.title,
                description = it.description,
                usage = it.usage,
                expireAt = it.expireAt
            ) }

    override fun toEntity(dto: DemandCancelErrorOnStatusDto): Mono<Demand> =
        dto.toMono().map { Demand(
            identifier = it.demandId,
            issuerId = it.issuerId,
            title = it.title,
            description = it.description,
            usage = it.usage,
            expireAt = it.expireAt
        ) }

    override fun toIpAssignDemandCreateMessage(demand: DemandDto): Mono<IpAssignDemandCreateMessage> =
        demand.toMono().map { IpAssignDemandCreateMessage(
            demandId = demand.id,
            issuerId = demand.issuerId
        ) }

    override fun toIpAssignDemandCancelMessage(demand: DemandDto): Mono<IpAssignDemandCancelMessage> =
        demand.toMono().map { IpAssignDemandCancelMessage(
            id = demand.id,
            issuerId = demand.issuerId,
            title = demand.title,
            description = demand.description,
            usage = demand.usage,
            expireAt = demand.expireAt
        ) }
}