package site.iplease.iadserver.domain.error.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.global.error.data.dto.DemandCreateErrorOnStatusDto
import site.iplease.iadserver.global.error.data.message.IpAssignDemandCreateErrorOnStatusMessage

interface DemandCreateErrorOnStatusConverter {
    fun convert(message: IpAssignDemandCreateErrorOnStatusMessage): Mono<DemandCreateErrorOnStatusDto>
}
