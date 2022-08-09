package site.iplease.iadserver.domain.cancel.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.global.cancel.data.dto.DemandCancelErrorOnStatusDto
import site.iplease.iadserver.global.cancel.data.message.IpAssignDemandCancelErrorOnStatusMessage

interface DemandCancelErrorOnStatusConverter {
    fun convert(message: IpAssignDemandCancelErrorOnStatusMessage): Mono<DemandCancelErrorOnStatusDto>
}
