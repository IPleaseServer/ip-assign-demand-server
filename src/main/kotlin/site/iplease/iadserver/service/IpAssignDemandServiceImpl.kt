package site.iplease.iadserver.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import site.iplease.iadserver.data.dto.DemandDto
import site.iplease.iadserver.repository.DemandRepository
import site.iplease.iadserver.util.DemandDataConverter

@Service
class IpAssignDemandServiceImpl(
    private val demandRepository: DemandRepository,
    private val demandDataConverter: DemandDataConverter
): IpAssignDemandService {
    override fun addDemand(demand: DemandDto): Mono<DemandDto> =
        demandDataConverter.toEntity(demand)
            .flatMap { entity -> demandRepository.save(entity) }
            .flatMap { entity -> demandDataConverter.toDto(entity) }
}