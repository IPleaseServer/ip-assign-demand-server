package site.iplease.iadserver.global.common.service

import reactor.core.publisher.Mono
import site.iplease.iadserver.global.common.data.dto.DemandDto

interface DemandQueryService {
    fun getDemandById(demandId: Long): Mono<DemandDto>
}
