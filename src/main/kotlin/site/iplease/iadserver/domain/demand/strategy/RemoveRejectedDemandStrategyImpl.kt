package site.iplease.iadserver.domain.demand.strategy

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.repository.DemandRepository
import site.iplease.iadserver.domain.demand.repository.RejectedDemandRepository

@Component
class RemoveRejectedDemandStrategyImpl(
    private val rejectedDemandRepository: RejectedDemandRepository,
    private val demandRepository: DemandRepository,
): RemoveRejectedDemandStrategy {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun removeRejectedDemand() {
        rejectedDemandRepository.selectAll()
            .parallel()
            .runOn(Schedulers.parallel())
            .flatMap { rejectedDemand -> demandRepository.deleteByIdentifier(rejectedDemand.demandId).then(rejectedDemand.toMono()) }
            .doOnRequest { logger.info("거절된 예약에 대한 일괄삭제작업을 진행합니다!") }
            .subscribe (
                { rejectedDemand -> logger.trace("Id가 ${rejectedDemand.demandId}인 예약을 제거하였습니다!") },
                { throwable ->
                    logger.warn("에약을 제거하던중 오류가 발생하였습니다!")
                    logger.warn("exception: ${throwable::class.simpleName}")
                    logger.warn("message: ${throwable.localizedMessage}")
                    logger.trace("stack trace")
                    logger.trace(throwable.stackTraceToString())
                }, { logger.info("예약 일괄삭제작업을 마쳤습니다!") })
    }
}