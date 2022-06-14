package site.iplease.iadserver.domain.demand.util

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.data.dto.IpAssignDemandErrorOnStatusDto
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandErrorOnStatusMessage

@Component
class DemandErrorOnStatusConverterImpl: DemandErrorOnStatusConverter {
    override fun convert(message: IpAssignDemandErrorOnStatusMessage): Mono<IpAssignDemandErrorOnStatusDto> =
        message.toMono()
            .map { IpAssignDemandErrorOnStatusDto(
                demandId = message.demandId,
                issuerId = message.issuerId,
                message = message.message
            ) }
}