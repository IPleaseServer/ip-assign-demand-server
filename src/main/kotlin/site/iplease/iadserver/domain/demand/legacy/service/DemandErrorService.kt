package site.iplease.iadserver.domain.demand.legacy.service

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.legacy.data.dto.DemandCancelErrorOnStatusDto
import site.iplease.iadserver.domain.demand.legacy.data.dto.DemandCreateErrorOnStatusDto

interface DemandErrorService {
    fun handle(demand: DemandCreateErrorOnStatusDto): Mono<Unit>
    fun handle(demand: DemandCancelErrorOnStatusDto): Mono<Unit>
}
