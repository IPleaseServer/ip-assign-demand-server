package site.iplease.iadserver.global.common.repository

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.common.data.entity.Demand

interface DemandSaver {
    fun saveDemand(demand: Demand, id: Long = System.currentTimeMillis()): Mono<Demand>
}