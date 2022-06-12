package site.iplease.iadserver.domain.demand.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.data.dto.IpAssignDemandErrorOnStatusDto
import site.iplease.iadserver.infra.alarm.service.PushAlarmService
import kotlin.random.Random
import kotlin.random.nextUInt

@Service
class DemandErrorServiceImpl(
    private val pushAlarmService: PushAlarmService
): DemandErrorService {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun errorOnStatus(demand: IpAssignDemandErrorOnStatusDto): Mono<Unit> =
        Unit.toMono()
            .map { createRandomId() }
            .flatMap { id -> logErrorOnStatus(id, demand) }
            .flatMap { id -> pushAlarmService.publish("할당IP신청중, 오류가 발생했습니다!", "다음 아이디로 관리자에게 문의해주세요!  - $id") }

    private fun logErrorOnStatus(id: String, demand: IpAssignDemandErrorOnStatusDto): Mono<String> =
        id.toMono()
            .map {
                logger.warn("[$id] 할당IP신청중 오류가 발생했습니다")
                logger.warn("[$id] demandId: ${demand.demandId}")
                logger.warn("[$id] message: ${demand.message}")
            }.map { id }

    //가끔 한두번은 겹쳐도 상관없다.
    private fun createRandomId() = Random.nextUInt().toString()
}