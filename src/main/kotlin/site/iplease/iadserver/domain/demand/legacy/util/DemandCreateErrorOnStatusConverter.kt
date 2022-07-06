package site.iplease.iadserver.domain.demand.legacy.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.legacy.data.dto.DemandCreateErrorOnStatusDto
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandCreateErrorOnStatusMessage

interface DemandCreateErrorOnStatusConverter {
    fun convert(message: IpAssignDemandCreateErrorOnStatusMessage): Mono<DemandCreateErrorOnStatusDto>
}
