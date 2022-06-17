package site.iplease.iadserver.domain.demand.service

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto

interface DemandQueryService {
    fun getDemandById(demandId: Long): Mono<DemandDto>
}
