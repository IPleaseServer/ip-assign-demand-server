package site.iplease.iadserver.domain.demand.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.data.entity.RejectedDemand
import site.iplease.iadserver.domain.demand.data.type.DemandPolicyType
import site.iplease.iadserver.domain.demand.repository.DemandRepository
import site.iplease.iadserver.domain.demand.repository.RejectedDemandRepository
import site.iplease.iadserver.domain.demand.util.DemandConverter
import site.iplease.iadserver.domain.demand.util.DemandPolicyValidator

@Service
@Qualifier("lazyReject")
class LazyRejectIpAssignDemandService(
    @Qualifier("lazyReject") private val demandPolicyValidator: DemandPolicyValidator,
    private val rejectedDemandRepository: RejectedDemandRepository,
    private val demandRepository: DemandRepository,
    private val demandConverter: DemandConverter,
    @Qualifier("impl") private val ipAssignDemandService: IpAssignDemandService
): IpAssignDemandService {
    override fun addDemand(demand: DemandDto): Mono<DemandDto> = ipAssignDemandService.addDemand(demand)
    override fun cancelDemand(demandId: Long): Mono<DemandDto> = ipAssignDemandService.cancelDemand(demandId)

    override fun acceptDemand(demandId: Long, assignIp: String): Mono<DemandDto> = ipAssignDemandService.acceptDemand(demandId, assignIp)

    override fun rejectDemand(demandId: Long, reason: String): Mono<DemandDto> =
        demandConverter.toDto(demandId)
            .flatMap { demand -> demandPolicyValidator.validate(demand, DemandPolicyType.DEMAND_REJECT) }
            .flatMap { _ -> demandRepository.findByIdentifier(demandId) }//DataStore에서 신청을 조회한다.
            .flatMap { entity -> demandConverter.toDto(entity) }//향후 반환값을 위해 조회한 신청을 Dto로 치환한다.
            .flatMap { dto -> rejectedDemandRepository.insert(RejectedDemand(dto.id, reason)).then(dto.toMono()) }//신청을 제거대기열에 추가하고, 미리 구성해둔 반환값을 발행한다.
}