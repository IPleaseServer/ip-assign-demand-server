package site.iplease.iadserver.domain.common.strategy

import reactor.core.publisher.Mono
import site.iplease.iadserver.global.common.data.dto.DemandDto

interface AddDemandStrategy {
    fun addDemand(demand: DemandDto): Mono<DemandDto>
}
