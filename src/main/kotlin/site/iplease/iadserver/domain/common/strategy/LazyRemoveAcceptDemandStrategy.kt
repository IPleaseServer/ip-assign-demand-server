package site.iplease.iadserver.domain.common.strategy

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.accept.data.entity.AcceptedDemand
import site.iplease.iadserver.domain.accept.repository.AcceptedDemandRepository
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.data.type.DemandPolicyType
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.global.common.util.DemandConverter
import site.iplease.iadserver.global.common.util.DemandPolicyValidator

@Component
class LazyRemoveAcceptDemandStrategy(
    private val demandConverter: DemandConverter,
    private val demandRepository: DemandRepository,
    private val acceptedDemandRepository: AcceptedDemandRepository,
    @Qualifier("lazyAccept") private val demandPolicyValidator: DemandPolicyValidator
): AcceptDemandStrategy {
    override fun acceptDemand(demandId: Long, assignIp: String): Mono<DemandDto> =
        demandConverter.toDto(demandId)
            .flatMap { demand -> demandPolicyValidator.validate(demand, DemandPolicyType.DEMAND_ACCEPT) }
            .flatMap { _ -> demandRepository.findByIdentifier(demandId) }
            .flatMap { entity -> demandConverter.toDto(entity) }
            .flatMap { dto -> acceptedDemandRepository.insert(AcceptedDemand(dto.id, assignIp)).then(dto.toMono()) }
}