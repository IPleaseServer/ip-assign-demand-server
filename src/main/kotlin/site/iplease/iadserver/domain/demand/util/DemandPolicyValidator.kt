package site.iplease.iadserver.domain.demand.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.data.type.DemandPolicyGroupType

interface DemandPolicyValidator {
    fun validate(demandId: Long, accountId: Long, policy: DemandPolicyGroupType): Mono<Unit>
}