package site.iplease.iadserver.domain.accept.strategy

import reactor.core.publisher.Mono
import site.iplease.iadserver.global.accept.data.dto.DemandAcceptedErrorOnManageDto

interface DemandAcceptCompensateStrategy {
    fun compensate(demand: DemandAcceptedErrorOnManageDto): Mono<Unit>
}