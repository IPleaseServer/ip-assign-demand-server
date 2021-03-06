package site.iplease.iadserver.domain.reject.strategy

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.domain.reject.repository.RejectedDemandRepository

@Component
class RemoveRejectedDemandStrategyImpl(
    private val rejectedDemandRepository: RejectedDemandRepository,
    private val demandRepository: DemandRepository,
): RemoveRejectedDemandStrategy {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun removeRejectedDemand() {
        Unit.toMono()
            .map { logger.info("거절된 예약에 대한 일괄삭제작업을 진행합니다!") }.toFlux()
            .flatMap { rejectedDemandRepository.selectAll() }
            .flatMap { rejectedDemand -> demandRepository.deleteByIdentifier(rejectedDemand.demandId).then(rejectedDemand.toMono()) }
            .flatMap { rejectedDemand ->  rejectedDemandRepository.delete(rejectedDemand.demandId).then(rejectedDemand.toMono()) }
            .subscribe (
                { rejectedDemand -> logger.trace("Id가 ${rejectedDemand.demandId}인 예약을 제거하였습니다!") },
                { throwable ->
                    logger.warn("거절된 신청을 제거하던중 오류가 발생하였습니다!")
                    logger.warn("exception: ${throwable::class.simpleName}")
                    logger.warn("message: ${throwable.localizedMessage}")
                    logger.trace("stack trace")
                    logger.trace(throwable.stackTraceToString())
                }, { logger.info("거절된 신청 일괄삭제작업을 마쳤습니다!") })
    }
}