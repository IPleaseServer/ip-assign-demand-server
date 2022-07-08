package site.iplease.iadserver.domain.reject.repository

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.reject.data.entity.RejectedDemand

interface RejectedDemandRepository {
    fun insert(rejectedDemand: RejectedDemand): Mono<Void>
    fun exist(id: Long): Mono<Boolean>
    fun selectAll(): Flux<RejectedDemand>
    fun delete(demandId: Long): Mono<Long>
}
