package site.iplease.iadserver.domain.demand.util

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.data.type.DemandPolicyType
import site.iplease.iadserver.domain.demand.exception.*
import site.iplease.iadserver.domain.demand.repository.DemandRepository
import site.iplease.iadserver.global.common.util.DateUtil

@Component
class DemandPolicyValidatorImpl(
    private val demandRepository: DemandRepository,
    private val dateUtil: DateUtil
): DemandPolicyValidator {
    override fun validate(demand: DemandDto, policy: DemandPolicyType): Mono<DemandDto> =
        policy.toMono().flatMap {
            when(it) {
                DemandPolicyType.DEMAND_CANCEL -> isExist(demand.id).flatMap { isOwner(demand.id, demand.issuerId) }.map { demand }
                DemandPolicyType.DEMAND_CREATE -> checkExpireAt(demand).flatMap { demand -> checkTitle(demand) }.map { demand.copy(id = 0) }
                DemandPolicyType.DEMAND_REJECT -> isExist(demand.id).map { demand }
            }
        }

    private fun isExist(demandId: Long, beExists: Boolean = true) =
        demandRepository.existsByIdentifier(demandId)
            .flatMap { isExist ->
                if(isExist == beExists) Unit.toMono()
                else if (beExists) Mono.error(DemandNotExistException("해당 ID를 가진 예약정보를 찾을 수 없습니다! - $demandId"))
                else Mono.error(DemandAlreadyExistsException("이미 해당 ID를 가진 예약정보가 존재합니다! - $demandId"))
            }

    private fun isOwner(demandId: Long, accountId: Long, beOwner: Boolean = true) =
        demandRepository.findByIdentifier(demandId)
            .map { demand -> demand.issuerId == accountId }
            .flatMap { isOwner ->
                if(isOwner == beOwner) Unit.toMono()
                else if (beOwner) Mono.error(NotOwnedDemandException("해당 계정이 소유한 에약이 아닙니다! - 예약: $demandId, 계정: $accountId"))
                else Mono.error(OwnedDemandException("해당 계정이 소유한 예약입니다! - 예약: $demandId, 계정: $accountId"))
            }

    private fun checkExpireAt(demand: DemandDto) =
        if(demand.expireAt.isAfter(dateUtil.dateNow())) demand.toMono()
        else Mono.error(WrongExpireDateException("만료일은 오늘 이후여야합니다!", demand.expireAt))

    private fun checkTitle(demand: DemandDto) =
        if(demand.title.length <= 25) demand.toMono()
        else Mono.error(WrongTitleException("신청 제목은 25자 이하여야합니다!"))
}