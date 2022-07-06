package site.iplease.iadserver.domain.demand.legacy.repository

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.legacy.data.entity.Demand

interface DemandSaver {
    fun saveDemand(demand: Demand, id: Long = System.currentTimeMillis()): Mono<Demand>
}