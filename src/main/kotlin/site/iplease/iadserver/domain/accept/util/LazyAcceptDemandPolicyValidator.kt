package site.iplease.iadserver.domain.accept.util

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.global.common.data.type.DemandPolicyType
import site.iplease.iadserver.domain.reject.exception.AlreadyRejectedDemandException
import site.iplease.iadserver.domain.accept.repository.AcceptedDemandRepository
import site.iplease.iadserver.domain.reject.repository.RejectedDemandRepository
import site.iplease.iadserver.global.common.util.DemandPolicyValidator
import site.iplease.iadserver.global.common.data.dto.DemandDto

@Component
@Qualifier("lazyAccept")
class LazyAcceptDemandPolicyValidator(
    @Qualifier("impl") private val demandPolicyValidator: DemandPolicyValidator,
    private val acceptedDemandRepository: AcceptedDemandRepository,
    private val rejectedDemandRepository: RejectedDemandRepository,
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
            .flatMap { isExists -> rejectedDemandRepository.exist(demand.id).map { isExists || it } }
            .flatMap { isExists ->
                if(isExists == beExists) Unit.toMono()
                else {// StatusManage서비스와 일관성이 깨져있을떄 발생함
                    logger.warn("IpAssignDemandStatusManageService와 IpAssignDemandService간의 데이터 정합성이 파손되었습니다.")
                    logger.warn("이미 ACCEPT/REJECT된 신청에 대한 ACCEPT Operation이 실행되었습니다.")
                    return@flatMap Mono.error(AlreadyRejectedDemandException("수락하거나 거절하신 신청은 재수락하실 수 없습니다! - ${demand.id}"))
                }
            }
}