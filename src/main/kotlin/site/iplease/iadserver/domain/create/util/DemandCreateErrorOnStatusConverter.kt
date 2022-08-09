package site.iplease.iadserver.domain.create.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.global.create.data.dto.DemandCreateErrorOnStatusDto
import site.iplease.iadserver.global.create.data.message.IpAssignDemandCreateErrorOnStatusMessage

interface DemandCreateErrorOnStatusConverter {
    fun convert(message: IpAssignDemandCreateErrorOnStatusMessage): Mono<DemandCreateErrorOnStatusDto>
}
