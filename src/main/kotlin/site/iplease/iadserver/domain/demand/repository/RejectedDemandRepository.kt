package site.iplease.iadserver.domain.demand.repository

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.data.entity.RejectedDemand

interface RejectedDemandRepository {
    fun insert(rejectedDemand: RejectedDemand): Mono<Void>
}
