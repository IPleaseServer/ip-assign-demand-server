package site.iplease.iadserver.domain.cancel.strategy

import reactor.core.publisher.Mono
import site.iplease.iadserver.global.cancel.data.dto.DemandCancelErrorOnStatusDto

interface DemandCancelCompensateStrategy {
    fun compensate(demand: DemandCancelErrorOnStatusDto): Mono<Unit>
}