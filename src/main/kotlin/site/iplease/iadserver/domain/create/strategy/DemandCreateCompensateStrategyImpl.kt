package site.iplease.iadserver.domain.create.strategy

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.global.common.util.DateUtil
import site.iplease.iadserver.global.create.data.dto.DemandCreateErrorOnStatusDto
import site.iplease.iadserver.infra.alarm.service.PushAlarmService
import kotlin.math.absoluteValue
import kotlin.random.Random

@Component
class DemandCreateCompensateStrategyImpl(
    private val demandRepository: DemandRepository,
    private val pushAlarmService: PushAlarmService,
    private val dateUtil: DateUtil
): DemandCreateCompensateStrategy {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun compensate(demand: DemandCreateErrorOnStatusDto): Mono<Unit> =
        demandRepository.deleteByIdentifier(demand.demandId)
            .then(Unit.toMono())

            .map { createRandomId() }
            .flatMap { id -> logErrorOnCreate(id, demand) }
            .flatMap { id -> pushAlarmService.publish(demand.issuerId, "할당IP신청중, 오류가 발생했습니다!", "다음 아이디로 관리자에게 문의해주세요!  - $id") }

    private fun logErrorOnCreate(id: String, demand: DemandCreateErrorOnStatusDto): Mono<String> =
        logErrorOnStatus(id, "할당IP신청중 오류가 발생했습니다!", issuerId = demand.issuerId, demandId = demand.demandId, errorMessage = demand.message)

    private fun logErrorOnStatus(id: String, description: String, issuerId: Long, demandId: Long, errorMessage: String): Mono<String> =
        id.toMono().map {
            logger.warn("[$id] $description")
            logger.warn("[$id] issuerId: $issuerId")
            logger.warn("[$id] demandId: $demandId")
            logger.warn("[$id] message: $errorMessage")
        }.map { id }

    //가끔 한두번은 겹쳐도 상관없다.
    private fun createRandomId() =  dateUtil.dateNow().dayOfMonth.toString() + Random.nextInt().absoluteValue.toString()
}