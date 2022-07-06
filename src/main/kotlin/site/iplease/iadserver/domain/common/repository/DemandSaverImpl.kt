package site.iplease.iadserver.domain.common.repository

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.common.data.entity.Demand
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.global.common.repository.DemandSaver

@Component
class DemandSaverImpl(
    private val demandRepository: DemandRepository
): DemandSaver {
    override fun saveDemand(demand: Demand, id: Long): Mono<Demand> =
        demandRepository.save(demand.copy(identifier = id))
}