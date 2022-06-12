package site.iplease.iadserver.domain.demand.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto

interface DemandDataValidator {
    fun validate(dto: DemandDto): Mono<DemandDto>
}