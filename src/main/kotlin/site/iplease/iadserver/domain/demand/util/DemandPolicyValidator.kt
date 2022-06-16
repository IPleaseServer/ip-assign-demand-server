package site.iplease.iadserver.domain.demand.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.data.type.DemandPolicyType

interface DemandPolicyValidator {
    fun validate(demand: DemandDto, policy: DemandPolicyType): Mono<DemandDto>
}