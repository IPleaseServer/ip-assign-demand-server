package site.iplease.iadserver.util

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.data.dto.DemandDto
import site.iplease.iadserver.data.request.AssignIpDemandRequest

@Component
class DemandDataConverterImpl: DemandDataConverter {
    override fun toDto(accountId: Long, request: AssignIpDemandRequest): Mono<DemandDto>  =
        Unit.toMono().map { DemandDto(
            issuerId = accountId,
            title = request.title,
            description = request.description,
            usage = request.usage,
            expireAt = request.expireAt
        ) }
}