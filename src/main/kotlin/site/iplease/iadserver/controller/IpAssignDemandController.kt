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
            demandDataConverter.toDto(accountId, request)
                .flatMap { demand -> demandDataValidator.validate(demand) }
                .flatMap { demand -> ipAssignDemandService.addDemand(demand) }
                .flatMap { demand -> messagePublishService.publish(MessageType.DEMAND_ASSIGN_IP, demand).map { demand } }
                .map { demand -> AssignIpDemandResponse(demandId = demand.id) }
                .map { response -> ResponseEntity.ok(response) }
}