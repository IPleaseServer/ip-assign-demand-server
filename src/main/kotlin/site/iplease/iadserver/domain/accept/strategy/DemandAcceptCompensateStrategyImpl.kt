package site.iplease.iadserver.domain.accept.strategy

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.accept.repository.AcceptedDemandRepository
import site.iplease.iadserver.global.accept.data.dto.DemandAcceptedErrorOnManageDto
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.global.common.repository.DemandSaver
import site.iplease.iadserver.global.common.util.DateUtil
import site.iplease.iadserver.global.common.util.DemandConverter
import kotlin.math.absoluteValue
import kotlin.random.Random

@Component
class DemandAcceptCompensateStrategyImpl(
    private val demandRepository: DemandRepository,
    private val demandConverter: DemandConverter,
    private val demandSaver: DemandSaver,
    private val acceptedDemandRepository: AcceptedDemandRepository,
    private val dateUtil: DateUtil
): DemandAcceptCompensateStrategy {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun compensate(demand: DemandAcceptedErrorOnManageDto): Mono<Unit> =
        //demandId에 대한 AssignIpDemand가 존재하지 않을경우 AssignIpDemand를 추가한다.
        demandRepository.existsByIdentifier(demand.demandId)
            .flatMap { isExists ->
                if(isExists) Mono.error(RuntimeException())
                else demandConverter.toEntity(demand)
            }.flatMap { demandSaver.saveDemand(it, demand.demandId) }
            .onErrorResume { Mono.empty() }
            //삭제 대기열에 demandId에 대한 AssignIpDemand가 존재할 경우 이를 제거한다.
            .flatMap { acceptedDemandRepository.delete(demand.demandId) }
            .map { createRandomId() }
            .flatMap { id -> logErrorOnAccepted(id, demand) }.map {  }

    private fun logErrorOnAccepted(id: String, demand: DemandAcceptedErrorOnManageDto): Mono<String> =
        logErrorOnStatus(id, "할당IP신청수락중 오류가 발생했습니다!", issuerId = demand.issuerId, demandId = demand.demandId, errorMessage = demand.message)

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