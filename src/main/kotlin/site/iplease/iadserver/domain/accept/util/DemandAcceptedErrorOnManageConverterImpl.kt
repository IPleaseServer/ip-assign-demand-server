package site.iplease.iadserver.domain.accept.util

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptedErrorOnManageMessage
import site.iplease.iadserver.global.accept.data.dto.DemandAcceptedErrorOnManageDto

@Component
class DemandAcceptedErrorOnManageConverterImpl: DemandAcceptedErrorOnManageConverter {
    override fun convert(message: IpAssignDemandAcceptedErrorOnManageMessage): Mono<DemandAcceptedErrorOnManageDto> =
        Unit.toMono().map { DemandAcceptedErrorOnManageDto(
            message.demandId,
            message.originStatus,
            message.issuerId,
            message.demandIssuerId,
            message.title,
            message.description,
            message.usage,
            message.expireAt,
            message.assignIp,
            message.message
        ) }
}