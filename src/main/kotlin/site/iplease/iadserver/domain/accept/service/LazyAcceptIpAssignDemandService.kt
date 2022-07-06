package site.iplease.iadserver.domain.accept.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.accept.data.entity.AcceptedDemand
import site.iplease.iadserver.domain.accept.repository.AcceptedDemandRepository
import site.iplease.iadserver.global.common.data.type.DemandPolicyType
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.global.common.util.DemandConverter
import site.iplease.iadserver.global.common.util.DemandPolicyValidator
import site.iplease.iadserver.global.common.service.IpAssignDemandService
import site.iplease.iadserver.global.common.data.dto.DemandDto

@Service
@Qualifier("lazyAccept")
class LazyAcceptIpAssignDemandService(
    @Qualifier("lazyAccept") private val demandPolicyValidator: DemandPolicyValidator,
    private val acceptedDemandRepository: AcceptedDemandRepository,
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
            .flatMap { dto -> acceptedDemandRepository.insert(AcceptedDemand(dto.id, assignIp)).then(dto.toMono()) }
}