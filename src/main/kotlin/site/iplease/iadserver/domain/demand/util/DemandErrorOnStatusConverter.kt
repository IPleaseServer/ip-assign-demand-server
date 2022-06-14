package site.iplease.iadserver.domain.demand.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.data.dto.IpAssignDemandErrorOnStatusDto
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandErrorOnStatusMessage

interface DemandErrorOnStatusConverter {
    fun convert(message: IpAssignDemandErrorOnStatusMessage): Mono<IpAssignDemandErrorOnStatusDto>
}
