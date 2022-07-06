package site.iplease.iadserver.domain.demand.legacy.service

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.legacy.data.dto.DemandDto

interface DemandQueryService {
    fun getDemandById(demandId: Long): Mono<DemandDto>
}
