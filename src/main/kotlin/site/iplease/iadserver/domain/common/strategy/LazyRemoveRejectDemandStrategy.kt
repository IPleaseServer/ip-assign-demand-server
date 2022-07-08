package site.iplease.iadserver.domain.common.strategy

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.reject.data.entity.RejectedDemand
import site.iplease.iadserver.domain.reject.repository.RejectedDemandRepository
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.data.type.DemandPolicyType
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.global.common.util.DemandConverter
import site.iplease.iadserver.global.common.util.DemandPolicyValidator

@Component
class LazyRemoveRejectDemandStrategy(
    private val demandConverter: DemandConverter,
    private val demandRepository: DemandRepository,
    private val rejectedDemandRepository: RejectedDemandRepository,
    @Qualifier("lazyAccept") private val demandPolicyValidator: DemandPolicyValidator
): RejectDemandStrategy {
    override fun rejectDemand(demandId: Long, reason: String): Mono<DemandDto> =
        demandConverter.toDto(demandId)
            .flatMap { demand -> demandPolicyValidator.validate(demand, DemandPolicyType.DEMAND_REJECT) }
            .flatMap { _ -> demandRepository.findByIdentifier(demandId) }//DataStore에서 신청을 조회한다.
            .flatMap { entity -> demandConverter.toDto(entity) }//향후 반환값을 위해 조회한 신청을 Dto로 치환한다.
            .flatMap { dto -> rejectedDemandRepository.insert(RejectedDemand(dto.id, reason)).then(dto.toMono()) }//신청을 제거대기열에 추가하고, 미리 구성해둔 반환값을 발행한다.
}