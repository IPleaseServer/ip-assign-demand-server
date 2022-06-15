package site.iplease.iadserver.domain.demand.controller

import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.data.request.AssignIpDemandRequest
import site.iplease.iadserver.domain.demand.data.request.CancelAssignIpDemandRequest
import site.iplease.iadserver.domain.demand.data.response.AssignIpDemandResponse
import site.iplease.iadserver.domain.demand.data.response.CancelAssignIpDemandResponse
import site.iplease.iadserver.domain.demand.data.type.AssignIpUsageType
import site.iplease.iadserver.domain.demand.data.type.DemandPolicyGroupType
import site.iplease.iadserver.infra.message.type.MessageType
import site.iplease.iadserver.domain.demand.service.IpAssignDemandService
import site.iplease.iadserver.infra.message.service.MessagePublishService
import site.iplease.iadserver.domain.demand.util.DemandDataConverter
import site.iplease.iadserver.domain.demand.util.DemandPolicyValidator
import java.time.LocalDate

@Validated
@RestController
@RequestMapping("/api/v1/demand/assign")
class IpAssignDemandController(
    private val ipAssignDemandService: IpAssignDemandService,
    private val messagePublishService: MessagePublishService,
    private val demandDataConverter: DemandDataConverter,
    private val demandPolicyValidator: DemandPolicyValidator
) {
    @DeleteMapping
    fun cancelAssignIpDemand(
        @RequestHeader("X-Authorization-Id") accountId: Long,
        @RequestBody request: CancelAssignIpDemandRequest
    ): Mono<ResponseEntity<CancelAssignIpDemandResponse>> =
        DemandDto(id = request.demandId, issuerId = accountId, "", "", AssignIpUsageType.USE_NETWORK, LocalDate.MAX).toMono()
            .flatMap { demand -> demandPolicyValidator.validate(demand, DemandPolicyGroupType.DEMAND_CANCEL) } //요청의 정보들을 검증한다.
            .flatMap { _-> ipAssignDemandService.cancelDemand(request.demandId) } //검증완료된 요청값을 통해 신청을 DataStore에서 삭제한다.
            .flatMap { demand -> publishCancelMessage(demand) } //예약 취소됨 메세지를 발행한다.
            .map { CancelAssignIpDemandResponse() } //반환값을 구성한다.
            .map { response -> ResponseEntity.ok(response) } //반환값을 ResponseEntity에 Wrapping하여 반환한다.

    @PostMapping
    fun demandAssignIp(@RequestHeader("X-Authorization-Id") accountId: Long,
                       @RequestBody request: AssignIpDemandRequest
    ): Mono<ResponseEntity<AssignIpDemandResponse>> =
            demandDataConverter.toDto(accountId, request) //요청정보에서 예약정보를 추출한다.
                .flatMap { demand -> demandPolicyValidator.validate(demand, DemandPolicyGroupType.DEMAND_CREATE) } //추출한 예약을 검증한다.
                .flatMap { demand -> ipAssignDemandService.addDemand(demand) } //검증완료된 예약을 추가한다.
                .flatMap { demand -> publishCreateMessage(demand) } //예약 추가됨 메세지를 발행한다.
                .map { demand -> AssignIpDemandResponse(demandId = demand.id) } //예약정보를 통해 반환값을 구성한다.
                .map { response -> ResponseEntity.ok(response) } //반환값을 ResponseEntity에 Wrapping하여 반환한다.


    private fun publishCancelMessage(demand: DemandDto) =
        demandDataConverter.toIpAssignDemandCancelMessage(demand)
            .flatMap { message -> messagePublishService.publish(MessageType.IP_ASSIGN_DEMAND_CANCEL, message)}
            .map { demand }

    private fun publishCreateMessage(demand: DemandDto) =
        demandDataConverter.toIpAssignDemandCreateMessage(demand)
            .flatMap { message -> messagePublishService.publish(MessageType.IP_ASSIGN_DEMAND_CREATE, message)}
            .map { demand }
}