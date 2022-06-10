package site.iplease.iadserver.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.data.dto.DemandDto

interface DemandDataValidator {
    fun validate(dto: DemandDto): Mono<DemandDto>
}