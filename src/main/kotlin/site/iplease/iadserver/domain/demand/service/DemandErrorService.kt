package site.iplease.iadserver.domain.demand.service

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.data.dto.IpAssignDemandErrorOnStatusDto

interface DemandErrorService {
    fun errorOnStatus(demand: IpAssignDemandErrorOnStatusDto): Mono<Unit>
}
