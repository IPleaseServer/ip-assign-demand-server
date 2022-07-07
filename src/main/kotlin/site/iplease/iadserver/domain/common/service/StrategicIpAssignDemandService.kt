package site.iplease.iadserver.domain.common.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.common.strategy.AddDemandStrategy
import site.iplease.iadserver.domain.common.strategy.RemoveDemandStrategy
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.service.IpAssignDemandService

@Service
class StrategicIpAssignDemandService(
    private val addDemandStrategy: AddDemandStrategy,
    private val removeDemandStrategy: RemoveDemandStrategy
): IpAssignDemandService {
    override fun addDemand(demand: DemandDto): Mono<DemandDto> = addDemandStrategy.addDemand(demand)

    override fun cancelDemand(demandId: Long): Mono<DemandDto> = removeDemandStrategy.removeDemand(demandId)
    override fun rejectDemand(demandId: Long, reason: String): Mono<DemandDto> = removeDemandStrategy.removeDemand(demandId)
    override fun acceptDemand(demandId: Long, assignIp: String): Mono<DemandDto> = removeDemandStrategy.removeDemand(demandId)
}