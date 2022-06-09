package site.iplease.iadserver.controller

import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.dto.DemandDto
import site.iplease.iadserver.exception.WrongExpireDateException
import site.iplease.iadserver.exception.WrongTitleException
import site.iplease.iadserver.request.AssignIpDemandRequest
import site.iplease.iadserver.response.AssignIpDemandResponse
import site.iplease.iadserver.service.IpAssignDemandService
import site.iplease.iadserver.util.DateUtil

@Validated
@RestController
@RequestMapping("/api/v1/demand/assign")
class IpAssignDemandController(
    private val ipAssignDemandService: IpAssignDemandService,
    private val dateUtil: DateUtil
) {
    @PostMapping
    fun demandAssignIp(@RequestHeader("X-Authorization-Id") accountId: Long,
                       @RequestBody request: AssignIpDemandRequest): Mono<ResponseEntity<AssignIpDemandResponse>> =
            makeDemandData(accountId, request)
                .flatMap { demand -> verifyDemandData(demand) }
                .flatMap { demand -> ipAssignDemandService.addDemand(demand) }
                .map { demand -> AssignIpDemandResponse(demandId = demand.id) }
                .map { response -> ResponseEntity.ok(response) }

    private fun makeDemandData(accountId: Long, request: AssignIpDemandRequest): Mono<DemandDto> =
        Unit.toMono().map { DemandDto(
                issuerId = accountId,
                title = request.title,
                description = request.description,
                usage = request.usage,
                expireAt = request.expireAt
            ) }

    private fun verifyDemandData(dto: DemandDto): Mono<DemandDto> =
        dto.toMono()
            .flatMap { demand ->
                if(demand.expireAt.isAfter(dateUtil.dateNow())) demand.toMono()
                else Mono.error(WrongExpireDateException("만료일은 오늘 이후여야합니다!", dto.expireAt))
            }.flatMap { demand ->
                if(demand.title.length <= 25) demand.toMono()
                else Mono.error(WrongTitleException("신청 제목은 25자 이하여야합니다!"))
            }
}