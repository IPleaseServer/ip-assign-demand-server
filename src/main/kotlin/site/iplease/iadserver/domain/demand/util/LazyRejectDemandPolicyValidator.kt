package site.iplease.iadserver.domain.demand.util

import org.slf4j.LoggerFactory
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
    private val logger = LoggerFactory.getLogger(this::class.java)

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
                else {// StatusManage서비스와 일관성이 깨져있을떄 발생함
                    logger.warn("IpAssignDemandStatusManageService와 IpAssignDemandService간의 데이터 정합성이 파손되었습니다.")
                    logger.warn("이미 REJECT된 예약에 대한 REJECT Operation이 실행되었습니다.")
                    return@flatMap Mono.error(AlreadyRejectedDemandException("거절하신 예약은 재거절하실 수 없습니다! - ${demand.id}"))
                }
            }
}