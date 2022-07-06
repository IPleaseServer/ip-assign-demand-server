package site.iplease.iadserver.domain.demand.legacy.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.legacy.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.legacy.data.type.DemandPolicyType

interface DemandPolicyValidator {
    fun validate(demand: DemandDto, policy: DemandPolicyType): Mono<DemandDto>
}