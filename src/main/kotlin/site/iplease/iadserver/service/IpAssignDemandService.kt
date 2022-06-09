package site.iplease.iadserver.service

import reactor.core.publisher.Mono
import site.iplease.iadserver.dto.DemandDto

interface IpAssignDemandService {
    fun addDemand(demand: DemandDto): Mono<DemandDto>

}
