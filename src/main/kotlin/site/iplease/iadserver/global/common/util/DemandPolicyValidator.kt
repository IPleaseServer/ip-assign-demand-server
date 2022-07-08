package site.iplease.iadserver.global.common.util

import reactor.core.publisher.Mono
import site.iplease.iadserver.global.common.data.type.DemandPolicyType
import site.iplease.iadserver.global.common.data.dto.DemandDto

interface DemandPolicyValidator {
    fun validate(demand: DemandDto, policy: DemandPolicyType): Mono<DemandDto>
}