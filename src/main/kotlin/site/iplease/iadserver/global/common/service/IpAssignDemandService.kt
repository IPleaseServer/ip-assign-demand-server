package site.iplease.iadserver.global.common.service

import reactor.core.publisher.Mono
import site.iplease.iadserver.global.common.data.dto.DemandDto

interface IpAssignDemandService {
    fun addDemand(demand: DemandDto): Mono<DemandDto>
    fun cancelDemand(demandId: Long): Mono<DemandDto>
    fun rejectDemand(demandId: Long, reason: String): Mono<DemandDto>
    fun acceptDemand(demandId: Long, assignIp: String): Mono<DemandDto>
}