package site.iplease.iadserver.domain.error.service

import reactor.core.publisher.Mono
import site.iplease.iadserver.global.error.data.dto.DemandAcceptedErrorOnManageDto
import site.iplease.iadserver.global.error.data.dto.DemandCancelErrorOnStatusDto
import site.iplease.iadserver.global.error.data.dto.DemandCreateErrorOnStatusDto

interface DemandErrorService {
    fun handle(demand: DemandCreateErrorOnStatusDto): Mono<Unit>
    fun handle(demand: DemandCancelErrorOnStatusDto): Mono<Unit>
    fun handle(demand: DemandAcceptedErrorOnManageDto): Mono<Unit>
}
