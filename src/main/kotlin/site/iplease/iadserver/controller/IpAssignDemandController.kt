package site.iplease.iadserver.controller

import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import site.iplease.iadserver.data.request.AssignIpDemandRequest
import site.iplease.iadserver.data.response.AssignIpDemandResponse
import site.iplease.iadserver.data.type.MessageType
import site.iplease.iadserver.service.IpAssignDemandService
import site.iplease.iadserver.service.MessagePublishService
import site.iplease.iadserver.util.DemandDataConverter
import site.iplease.iadserver.util.DemandDataValidator

@Validated
@RestController
@RequestMapping("/api/v1/demand/assign")
class IpAssignDemandController(
    private val ipAssignDemandService: IpAssignDemandService,
    private val messagePublishService: MessagePublishService,
    private val demandDataConverter: DemandDataConverter,
    private val demandDataValidator: DemandDataValidator
) {
    @PostMapping
    fun demandAssignIp(@RequestHeader("X-Authorization-Id") accountId: Long,
                       @RequestBody request: AssignIpDemandRequest
    ): Mono<ResponseEntity<AssignIpDemandResponse>> =
            demandDataConverter.toDto(accountId, request) //요청정보에서 예약정보를 추출한다.
                .flatMap { demand -> demandDataValidator.validate(demand) } //추출한 예약을 검증한다.
                .flatMap { demand -> ipAssignDemandService.addDemand(demand) } //검증완료된 예약을 추가한다.
                .flatMap { demand -> messagePublishService.publish(MessageType.IP_ASSIGN_DEMAND_CREATE, demand).map { demand } } //예약 추가됨 메세지를 발행한다.
                .map { demand -> AssignIpDemandResponse(demandId = demand.id) } //예약정보를 통해 반환값을 구성한다.
                .map { response -> ResponseEntity.ok(response) } //반환값을 ResponseEntity에 Wrapping하여 반환한다.
}