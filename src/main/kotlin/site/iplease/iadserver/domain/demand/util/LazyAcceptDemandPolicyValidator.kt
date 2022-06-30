package site.iplease.iadserver.domain.demand.util

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.data.type.DemandPolicyType
import site.iplease.iadserver.domain.demand.exception.AlreadyRejectedDemandException
import site.iplease.iadserver.domain.demand.repository.AcceptedDemandRepository

@Component
@Qualifier("lazyAccept")
class LazyAcceptDemandPolicyValidator(
    @Qualifier("impl") private val demandPolicyValidator: DemandPolicyValidator,
    private val acceptedDemandRepository: AcceptedDemandRepository
): DemandPolicyValidator {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun validate(demand: DemandDto, policy: DemandPolicyType): Mono<DemandDto> =
        demandPolicyValidator.validate(demand, policy)
            .flatMap { dto ->
                if(policy == DemandPolicyType.DEMAND_ACCEPT) isExistInAcceptedDemand(dto, false).map { dto }
                else dto.toMono()
            }

    private fun isExistInAcceptedDemand(demand: DemandDto, beExists: Boolean = true): Mono<Unit> =
        acceptedDemandRepository.exist(demand.id)
            .flatMap { isExists ->
                if(isExists == beExists) Unit.toMono()
                else {// StatusManage서비스와 일관성이 깨져있을떄 발생함
                    logger.warn("IpAssignDemandStatusManageService와 IpAssignDemandService간의 데이터 정합성이 파손되었습니다.")
                    logger.warn("이미 ACCEPT된 예약에 대한 ACCEPT Operation이 실행되었습니다.")
                    return@flatMap Mono.error(AlreadyRejectedDemandException("수락하신 예약은 재수락하실 수 없습니다! - ${demand.id}"))
                }
            }
}