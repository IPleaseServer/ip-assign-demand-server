package site.iplease.iadserver.domain.error.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.error.data.dto.DemandCancelErrorOnStatusDto
import site.iplease.iadserver.global.error.data.message.IpAssignDemandCancelErrorOnStatusMessage

interface DemandCancelErrorOnStatusConverter {
    fun convert(message: IpAssignDemandCancelErrorOnStatusMessage): Mono<DemandCancelErrorOnStatusDto>
}
