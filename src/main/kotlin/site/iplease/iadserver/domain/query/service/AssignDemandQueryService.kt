package site.iplease.iadserver.domain.query.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import reactor.core.publisher.Mono
import site.iplease.iadserver.global.common.data.dto.DemandDto

interface AssignDemandQueryService {
    fun getAllAssignDemand(page: PageRequest): Mono<Page<DemandDto>>
    fun getAllAssignDemandByIssuerId(page: PageRequest, issuerId: Long): Mono<Page<DemandDto>>
    fun getAssignDemandById(demandId: Long): Mono<DemandDto>

}
