package site.iplease.iadserver.domain.demand.legacy.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.legacy.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.legacy.exception.DemandNotExistException
import site.iplease.iadserver.domain.demand.legacy.repository.DemandRepository
import site.iplease.iadserver.domain.demand.legacy.util.DemandConverter

@Service
class DemandQueryServiceImpl(
    private val demandRepository: DemandRepository,
    private val demandConverter: DemandConverter
): DemandQueryService {
    override fun getDemandById(demandId: Long): Mono<DemandDto> =
        demandRepository.existsByIdentifier(demandId)
            .flatMap { isExists ->
                if(isExists) demandRepository.findByIdentifier(demandId)
                else Mono.error(DemandNotExistException("해당 ID를 가지는 신청정보를 찾을 수 없습니다! - $demandId"))
            }.flatMap { entity -> demandConverter.toDto(entity) }
}