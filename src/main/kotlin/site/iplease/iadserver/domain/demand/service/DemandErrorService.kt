package site.iplease.iadserver.domain.demand.service

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.data.dto.DemandCancelErrorOnStatusDto
import site.iplease.iadserver.domain.demand.data.dto.DemandCreateErrorOnStatusDto

interface DemandErrorService {
    fun handle(demand: DemandCreateErrorOnStatusDto): Mono<Unit>
    fun handle(demand: DemandCancelErrorOnStatusDto): Mono<Unit>
}
