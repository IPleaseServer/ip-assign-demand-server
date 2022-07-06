package site.iplease.iadserver.domain.demand.legacy.repository

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.legacy.data.entity.AcceptedDemand

interface AcceptedDemandRepository {
    fun insert(acceptedDemand: AcceptedDemand): Mono<Void>
    fun exist(id: Long): Mono<Boolean>
    fun selectAll(): Flux<AcceptedDemand>
    fun delete(demandId: Long): Mono<Long>
}
