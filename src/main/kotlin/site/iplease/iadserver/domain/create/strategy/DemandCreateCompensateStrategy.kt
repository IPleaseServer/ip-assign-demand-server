package site.iplease.iadserver.domain.create.strategy

import reactor.core.publisher.Mono
import site.iplease.iadserver.global.create.data.dto.DemandCreateErrorOnStatusDto

interface DemandCreateCompensateStrategy {
    fun compensate(demand: DemandCreateErrorOnStatusDto): Mono<Unit>
}