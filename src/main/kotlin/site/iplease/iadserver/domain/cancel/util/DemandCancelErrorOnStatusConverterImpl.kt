package site.iplease.iadserver.domain.cancel.util

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.global.cancel.data.dto.DemandCancelErrorOnStatusDto
import site.iplease.iadserver.global.cancel.data.message.IpAssignDemandCancelErrorOnStatusMessage

@Component
class DemandCancelErrorOnStatusConverterImpl: DemandCancelErrorOnStatusConverter {
    override fun convert(message: IpAssignDemandCancelErrorOnStatusMessage): Mono<DemandCancelErrorOnStatusDto> =
        message.toMono().map { DemandCancelErrorOnStatusDto( //TODO Unit.toMono().map 으로 바꾸기
            demandId =  message.id,
            issuerId =  message.issuerId,
            title =  message.title,
            description =  message.description,
            usage =  message.usage,
            expireAt =  message.expireAt,
            message = message.message
        ) }
}