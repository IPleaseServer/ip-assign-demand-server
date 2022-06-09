package site.iplease.iadserver.service

import reactor.core.publisher.Mono
import site.iplease.iadserver.data.dto.DemandDto

interface IpAssignDemandService {
    fun addDemand(demand: DemandDto): Mono<DemandDto>

}
