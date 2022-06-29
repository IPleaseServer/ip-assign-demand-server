package site.iplease.iadserver.domain.demand.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.data.entity.AcceptedDemand
import site.iplease.iadserver.domain.demand.data.type.DemandPolicyType
import site.iplease.iadserver.domain.demand.repository.AcceptedDemandRepository
import site.iplease.iadserver.domain.demand.repository.DemandRepository
import site.iplease.iadserver.domain.demand.util.DemandConverter
import site.iplease.iadserver.domain.demand.util.DemandPolicyValidator

@Service
@Qualifier("lazyAccept")
class LazyAcceptIpAssignDemandService(
    @Qualifier("lazyAccept") private val demandPolicyValidator: DemandPolicyValidator,
    private val accpetedDemandRepository: AcceptedDemandRepository,
    private val demandRepository: DemandRepository,
    private val demandConverter: DemandConverter,
    @Qualifier("impl") private val ipAssignDemandService: IpAssignDemandService
): IpAssignDemandService {
    override fun addDemand(demand: DemandDto): Mono<DemandDto> = ipAssignDemandService.addDemand(demand)

    override fun cancelDemand(demandId: Long): Mono<DemandDto> = ipAssignDemandService.cancelDemand(demandId)

    override fun rejectDemand(demandId: Long, reason: String): Mono<DemandDto> = ipAssignDemandService.rejectDemand(demandId, reason)

    override fun acceptDemand(demandId: Long, assignIp: String): Mono<DemandDto> =
        demandConverter.toDto(demandId)
            .flatMap { demand -> demandPolicyValidator.validate(demand, DemandPolicyType.DEMAND_ACCEPT) }
            .flatMap { _ -> demandRepository.findByIdentifier(demandId) }
            .flatMap { entity -> demandConverter.toDto(entity) }
            .flatMap { dto -> accpetedDemandRepository.insert(AcceptedDemand(dto.id, assignIp)).then(dto.toMono()) }
}