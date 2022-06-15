package site.iplease.iadserver.domain.demand.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.data.dto.IpAssignDemandErrorOnStatusDto
import site.iplease.iadserver.domain.demand.repository.DemandRepository
import site.iplease.iadserver.global.common.util.DateUtil
import site.iplease.iadserver.infra.alarm.service.PushAlarmService
import kotlin.math.absoluteValue
import kotlin.random.Random

@Service
class DemandErrorServiceImpl(
    private val pushAlarmService: PushAlarmService,
    private val demandRepository: DemandRepository,
    private val dateUtil: DateUtil
): DemandErrorService {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun handle(demand: IpAssignDemandErrorOnStatusDto): Mono<Unit> =
        demandRepository.deleteById(demand.demandId)
            .then(Unit.toMono())
            .map { createRandomId() }
            .flatMap { id -> logErrorOnStatus(id, demand) }
            .flatMap { id -> pushAlarmService.publish(demand.issuerId, "할당IP신청중, 오류가 발생했습니다!", "다음 아이디로 관리자에게 문의해주세요!  - $id") }

    private fun logErrorOnStatus(id: String, demand: IpAssignDemandErrorOnStatusDto): Mono<String> =
        id.toMono()
            .map {
                logger.warn("[$id] 할당IP신청중 오류가 발생했습니다")
                logger.warn("[$id] issuerId: ${demand.issuerId}")
                logger.warn("[$id] demandId: ${demand.demandId}")
                logger.warn("[$id] message: ${demand.message}")
            }.map { id }

    //가끔 한두번은 겹쳐도 상관없다.
    private fun createRandomId() =  dateUtil.dateNow().dayOfMonth.toString() + Random.nextInt().absoluteValue.toString()
}