package site.iplease.iadserver.domain.demand.service

import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto

interface IpAssignDemandService {
    fun addDemand(demand: DemandDto): Mono<DemandDto>

}
