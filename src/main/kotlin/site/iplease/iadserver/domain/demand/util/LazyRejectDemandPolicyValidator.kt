package site.iplease.iadserver.domain.demand.util

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.data.type.DemandPolicyType
import site.iplease.iadserver.domain.demand.exception.AlreadyRejectedDemandException
import site.iplease.iadserver.domain.demand.repository.RedisRejectedDemandRepository

@Component
@Qualifier("lazyReject")
class LazyRejectDemandPolicyValidator(
    @Qualifier("impl") private val demandPolicyValidator: DemandPolicyValidator,
    private val rejectedDemandRepository: RedisRejectedDemandRepository
): DemandPolicyValidator {
    override fun validate(demand: DemandDto, policy: DemandPolicyType): Mono<DemandDto> =
        demandPolicyValidator.validate(demand, policy)
            .flatMap { dto ->
                if(policy == DemandPolicyType.DEMAND_REJECT) isExistsInRejectedDemand(dto, false).map { dto }
                else dto.toMono()
            }

    private fun isExistsInRejectedDemand(demand: DemandDto, beExists: Boolean = true): Mono<Unit> =
        rejectedDemandRepository.exist(demand.id)
            .flatMap { isExists ->
                if(isExists == beExists) Unit.toMono()
                else Mono.error(AlreadyRejectedDemandException("거절하신 예약은 재거절하실 수 없습니다! - ${demand.id}"))// StatusManage서비스와 일관성이 깨져있을떄 발생함
            }
}