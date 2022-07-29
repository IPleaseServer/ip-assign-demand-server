package site.iplease.iadserver.domain.common.controller

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.common.data.request.CreateAssignIpDemandRequest
import site.iplease.iadserver.domain.common.data.request.CancelAssignIpDemandRequest
import site.iplease.iadserver.domain.common.data.response.CreateAssignIpDemandResponse
import site.iplease.iadserver.domain.common.data.response.CancelAssignIpDemandResponse
import site.iplease.iadserver.global.common.data.type.AssignIpUsageType
import site.iplease.iadserver.global.common.data.type.DemandPolicyType
import site.iplease.iadserver.infra.message.type.MessageType
import site.iplease.iadserver.global.common.service.IpAssignDemandService
import site.iplease.iadserver.infra.message.service.MessagePublishService
import site.iplease.iadserver.global.common.util.DemandConverter
import site.iplease.iadserver.global.common.util.DemandPolicyValidator
import site.iplease.iadserver.global.common.data.dto.DemandDto
import java.time.LocalDate

@Validated
@RestController
@RequestMapping("/api/v1/demand/assign")
class IpAssignDemandController(
    private val ipAssignDemandService: IpAssignDemandService,//IP할당 거절기능 추가시 lazyReject로 바꾸어야합니다.
    private val messagePublishService: MessagePublishService,
    private val demandConverter: DemandConverter,
    @Qualifier("impl") private val demandPolicyValidator: DemandPolicyValidator
) {
    @DeleteMapping
    fun cancelAssignIpDemand(
        @RequestHeader("X-Authorization-Id") accountId: Long,
        @RequestBody request: CancelAssignIpDemandRequest
    ): Mono<ResponseEntity<CancelAssignIpDemandResponse>> =
        DemandDto(id = request.demandId, issuerId = accountId, "", "", AssignIpUsageType.USE_NETWORK, LocalDate.MAX).toMono()
            .flatMap { demand -> demandPolicyValidator.validate(demand, DemandPolicyType.DEMAND_CANCEL) } //요청의 정보들을 검증한다.
            .flatMap { _-> ipAssignDemandService.cancelDemand(request.demandId) } //검증완료된 요청값을 통해 신청을 DataStore에서 삭제한다.
            .flatMap { demand -> publishCancelMessage(demand) } //예약 취소됨 메세지를 발행한다.
            .map { CancelAssignIpDemandResponse() } //반환값을 구성한다.
            .map { response -> ResponseEntity.ok(response) } //반환값을 ResponseEntity에 Wrapping하여 반환한다.

    @PostMapping
    fun createAssignIpDemand(@RequestHeader("X-Authorization-Id") accountId: Long,
                             @RequestBody request: CreateAssignIpDemandRequest
    ): Mono<ResponseEntity<CreateAssignIpDemandResponse>> =
            demandConverter.toDto(accountId, request) //요청정보에서 예약정보를 추출한다.
                .flatMap { demand -> demandPolicyValidator.validate(demand, DemandPolicyType.DEMAND_CREATE) } //추출한 예약을 검증한다.
                .flatMap { demand -> ipAssignDemandService.addDemand(demand) } //검증완료된 예약을 추가한다.
                .flatMap { demand -> publishCreateMessage(demand) } //예약 추가됨 메세지를 발행한다.
                .map { demand -> CreateAssignIpDemandResponse(demandId = demand.id) } //예약정보를 통해 반환값을 구성한다.
                .map { response -> ResponseEntity.ok(response) } //반환값을 ResponseEntity에 Wrapping하여 반환한다.


    private fun publishCancelMessage(demand: DemandDto) =
        demandConverter.toIpAssignDemandCancelMessage(demand)
            .flatMap { message -> messagePublishService.publish(MessageType.IP_ASSIGN_DEMAND_CANCEL, message)}
            .map { demand }

    private fun publishCreateMessage(demand: DemandDto) =
        demandConverter.toIpAssignDemandCreateMessage(demand)
            .flatMap { message -> messagePublishService.publish(MessageType.IP_ASSIGN_DEMAND_CREATE, message)}
            .map { demand }
}