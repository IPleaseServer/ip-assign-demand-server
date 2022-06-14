package site.iplease.iadserver.domain.demand.util

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.data.type.DemandPolicyGroupType
import site.iplease.iadserver.domain.demand.exception.DemandAlreadyExistsException
import site.iplease.iadserver.domain.demand.exception.DemandNotExistException
import site.iplease.iadserver.domain.demand.exception.NotOwnedDemandException
import site.iplease.iadserver.domain.demand.exception.OwnedDemandException
import site.iplease.iadserver.domain.demand.repository.DemandRepository

@Component
class DemandPolicyValidatorImpl(
    private val demandRepository: DemandRepository
): DemandPolicyValidator {
    override fun validate(demandId: Long, accountId: Long, policy: DemandPolicyGroupType): Mono<Unit> =
        policy.toMono().flatMap {
            when(it) {
                DemandPolicyGroupType.DEMAND_CANCEL -> isExist(demandId).flatMap { isOwner(demandId, accountId) }
            }
        }

    private fun isExist(demandId: Long, beExists: Boolean = true) =
        demandRepository.existsById(demandId)
            .flatMap { isExist ->
                if(isExist == beExists) Unit.toMono()
                else if (beExists) Mono.error(DemandNotExistException("해당 ID를 가진 예약정보를 찾을 수 없습니다! - $demandId"))
                else Mono.error(DemandAlreadyExistsException("이미 해당 ID를 가진 예약정보가 존재합니다! - $demandId"))
            }

    private fun isOwner(demandId: Long, accountId: Long, beOwner: Boolean = true) =
        demandRepository.findById(demandId)
            .map { demand -> demand.issuerId == accountId }
            .flatMap { isOwner ->
                if(isOwner == beOwner) Unit.toMono()
                else if (beOwner) Mono.error(NotOwnedDemandException("해당 계정이 소유한 에약이 아닙니다! - 예약: $demandId, 계정: $accountId"))
                else Mono.error(OwnedDemandException("해당 계정이 소유한 예약입니다! - 예약: $demandId, 계정: $accountId"))
            }
}