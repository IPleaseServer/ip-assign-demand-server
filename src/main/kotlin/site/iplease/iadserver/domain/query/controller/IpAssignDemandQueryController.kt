package site.iplease.iadserver.domain.query.controller

import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.query.config.DataQueryProperty
import site.iplease.iadserver.domain.query.response.AssignDemandQueryResponse
import site.iplease.iadserver.domain.query.response.PagableAssignDemandQueryResponse
import site.iplease.iadserver.domain.query.service.AssignDemandQueryService

@RestController
@RequestMapping("/api/v1/demand/assign/query")
class IpAssignDemandQueryController(
    private val dataQueryProperty: DataQueryProperty,
    private val assignDemandQueryService: AssignDemandQueryService
) {
    @GetMapping("/all")
    fun getAllAssignDemand(@RequestParam page: Int): Mono<ResponseEntity<PagableAssignDemandQueryResponse>> =
        PageRequest.of(page, dataQueryProperty.all.pageSize).toMono()
            .flatMap { assignDemandQueryService.getAllAssignDemand(it) }
            .map { PagableAssignDemandQueryResponse(it) }
            .map { ResponseEntity.ok(it) }

    @GetMapping("/issuer")
    fun getAssignDemandByIssuerId(@RequestParam page: Int, @RequestParam issuerId: Long): Mono<ResponseEntity<PagableAssignDemandQueryResponse>> =
        PageRequest.of(page, dataQueryProperty.byIssuer.pageSize).toMono()
            .flatMap { assignDemandQueryService.getAllAssignDemandByIssuerId(it, issuerId) }
            .map { PagableAssignDemandQueryResponse(it) }
            .map { ResponseEntity.ok(it) }

    @GetMapping("/{demandId}")
    fun getAssignDemandById(@PathVariable demandId: Long): Mono<ResponseEntity<AssignDemandQueryResponse>> =
        assignDemandQueryService.getAssignDemandById(demandId)
            .map { AssignDemandQueryResponse(it) }
            .map { ResponseEntity.ok(it) }
}