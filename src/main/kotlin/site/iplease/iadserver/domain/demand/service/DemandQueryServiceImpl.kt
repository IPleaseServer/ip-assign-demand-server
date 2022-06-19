package site.iplease.iadserver.domain.demand.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.exception.DemandNotExistException
import site.iplease.iadserver.domain.demand.repository.DemandRepository
import site.iplease.iadserver.domain.demand.util.DemandConverter

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