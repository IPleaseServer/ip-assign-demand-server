package site.iplease.iadserver.domain.demand.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.data.dto.DemandCreateErrorOnStatusDto
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandCreateErrorOnStatusMessage

interface DemandCreateErrorOnStatusConverter {
    fun convert(message: IpAssignDemandCreateErrorOnStatusMessage): Mono<DemandCreateErrorOnStatusDto>
}
