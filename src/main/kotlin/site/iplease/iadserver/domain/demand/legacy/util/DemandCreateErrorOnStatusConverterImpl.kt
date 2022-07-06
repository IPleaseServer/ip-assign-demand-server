package site.iplease.iadserver.domain.demand.legacy.util

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.legacy.data.dto.DemandCreateErrorOnStatusDto
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandCreateErrorOnStatusMessage

@Component
class DemandCreateErrorOnStatusConverterImpl: DemandCreateErrorOnStatusConverter {
    override fun convert(message: IpAssignDemandCreateErrorOnStatusMessage): Mono<DemandCreateErrorOnStatusDto> =
        message.toMono()
            .map { DemandCreateErrorOnStatusDto(
                demandId = message.demandId,
                issuerId = message.issuerId,
                message = message.message
            ) }
}