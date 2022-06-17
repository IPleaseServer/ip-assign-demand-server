package site.iplease.iadserver.domain.demand.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.repository.DemandRepository
import site.iplease.iadserver.domain.demand.util.DemandConverter

@Service
class DemandQueryServiceImpl(
    private val demandRepository: DemandRepository,
    private val demandConverter: DemandConverter
): DemandQueryService {
    override fun getDemandById(demandId: Long): Mono<DemandDto> =
        demandRepository.findByIdentifier(demandId)
            .flatMap { entity -> demandConverter.toDto(entity) }
}