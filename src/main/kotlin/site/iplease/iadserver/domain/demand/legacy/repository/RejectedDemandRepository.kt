package site.iplease.iadserver.domain.demand.legacy.repository

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.legacy.data.entity.RejectedDemand

interface RejectedDemandRepository {
    fun insert(rejectedDemand: RejectedDemand): Mono<Void>
    fun exist(id: Long): Mono<Boolean>
    fun selectAll(): Flux<RejectedDemand>
    fun delete(demandId: Long): Mono<Long>
}
