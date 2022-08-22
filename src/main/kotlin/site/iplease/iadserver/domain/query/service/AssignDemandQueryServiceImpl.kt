package site.iplease.iadserver.domain.query.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.common.data.entity.Demand
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.global.common.util.DemandConverter

@Service
class AssignDemandQueryServiceImpl(
    private val demandRepository: DemandRepository,
    private val demandConverter: DemandConverter
): AssignDemandQueryService {
    override fun getAllAssignDemand(page: PageRequest): Mono<Page<DemandDto>> = demandRepository.findBy(page).convert()

    override fun getAllAssignDemandByIssuerId(page: PageRequest, issuerId: Long): Mono<Page<DemandDto>> = demandRepository.findByIssuerId(page, issuerId).convert()

    override fun getAssignDemandById(demandId: Long): Mono<DemandDto> = demandRepository.findByIdentifier(demandId).flatMap { demandConverter.toDto(it) }

    private fun Flux<Demand>.convert(): Mono<Page<DemandDto>> =
        flatMap { demandConverter.toDto(it) }
            .collectList()
            .map { PageImpl(it) }
}