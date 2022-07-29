package site.iplease.iadserver.domain.common.util

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.global.error.data.dto.DemandCancelErrorOnStatusDto
import site.iplease.iadserver.domain.common.data.entity.Demand
import site.iplease.iadserver.domain.common.data.request.AssignIpDemandRequest
import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptMessage
import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptedMessage
import site.iplease.iadserver.global.common.data.type.AssignIpUsageType
import site.iplease.iadserver.global.common.util.DemandConverter
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.data.message.IpAssignDemandCancelMessage
import site.iplease.iadserver.global.common.data.message.IpAssignDemandCreateMessage
import site.iplease.iadserver.global.confirm.data.message.IpAssignDemandConfirmMessage
import site.iplease.iadserver.global.error.data.dto.DemandAcceptedErrorOnManageDto
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

    override fun toDto(demandId: Long): Mono<DemandDto> =
        Unit.toMono().map { DemandDto(
            id = demandId,
            issuerId = 0,
            title = "@title@",
            description = "@description@",
            usage = AssignIpUsageType.USE_NETWORK,
            expireAt = LocalDate.MAX
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
            title = "@title@",
            description = "@description@",
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

    override fun toEntity(demand: DemandAcceptedErrorOnManageDto): Mono<Demand> =
        demand.toMono().map { Demand(
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

    override fun toAssignIpCreateMessage(
        demand: DemandDto,
        message: IpAssignDemandAcceptMessage
    ): Mono<IpAssignDemandAcceptedMessage> =
        demand.toMono().map { IpAssignDemandAcceptedMessage(
            assignIp = message.assignIp,
            issuerId = message.issuerId,
            originStatus = message.originStatus,
            demandId = message.demandId,
            demandIssuerId = demand.issuerId,
            title = demand.title,
            description = demand.description,
            usage = demand.usage,
            expireAt = demand.expireAt,
        ) }
}