package site.iplease.iadserver.domain.demand.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.data.type.DemandPolicyGroupType

interface DemandPolicyValidator {
    fun validate(demand: DemandDto, policy: DemandPolicyGroupType): Mono<DemandDto>
}