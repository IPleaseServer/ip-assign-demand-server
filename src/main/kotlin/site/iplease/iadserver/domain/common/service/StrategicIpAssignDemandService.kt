package site.iplease.iadserver.domain.common.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.common.strategy.AcceptDemandStrategy
import site.iplease.iadserver.domain.common.strategy.AddDemandStrategy
import site.iplease.iadserver.domain.common.strategy.CancelDemandStrategy
import site.iplease.iadserver.domain.common.strategy.RejectDemandStrategy
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.service.IpAssignDemandService

@Service
class StrategicIpAssignDemandService(
    private val addDemandStrategy: AddDemandStrategy,
    private val cancelDemandStrategy: CancelDemandStrategy,
    private val rejectDemandStrategy: RejectDemandStrategy,
    private val acceptDemandStrategy: AcceptDemandStrategy
): IpAssignDemandService {
    override fun addDemand(demand: DemandDto): Mono<DemandDto> = addDemandStrategy.addDemand(demand)

    override fun cancelDemand(demandId: Long): Mono<DemandDto> = cancelDemandStrategy.cancelDemand(demandId)
    override fun rejectDemand(demandId: Long, reason: String): Mono<DemandDto> = rejectDemandStrategy.rejectDemand(demandId, reason)
    override fun acceptDemand(demandId: Long, assignIp: String): Mono<DemandDto> = acceptDemandStrategy.acceptDemand(demandId, assignIp)
}