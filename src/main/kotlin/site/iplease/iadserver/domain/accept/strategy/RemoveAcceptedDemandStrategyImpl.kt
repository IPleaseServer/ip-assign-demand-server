package site.iplease.iadserver.domain.accept.strategy

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.accept.repository.AcceptedDemandRepository
import site.iplease.iadserver.global.common.repository.DemandRepository

@Component
class RemoveAcceptedDemandStrategyImpl(
    private val acceptedDemandRepository: AcceptedDemandRepository,
    private val demandRepository: DemandRepository
): RemoveAcceptedDemandStrategy {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun removeAcceptedDemand() {
        Unit.toMono()
            .map { logger.info("수락된 예약에 대한 일괄삭제작업을 진행합니다!") }.toFlux()
            .flatMap { acceptedDemandRepository.selectAll() }
            .flatMap { acceptedDemand -> demandRepository.deleteByIdentifier(acceptedDemand.demandId).then(acceptedDemand.toMono()) }
            .flatMap { acceptedDemand ->  acceptedDemandRepository.delete(acceptedDemand.demandId).then(acceptedDemand.toMono()) }
            .subscribe (
                { acceptedDemand -> logger.trace("Id가 ${acceptedDemand.demandId}인 예약을 제거하였습니다!") },
                { throwable ->
                    logger.warn("수락된 신청을 제거하던중 오류가 발생하였습니다!")
                    logger.warn("exception: ${throwable::class.simpleName}")
                    logger.warn("message: ${throwable.localizedMessage}")
                    logger.trace("stack trace")
                    logger.trace(throwable.stackTraceToString())
                }, { logger.info("수락된 신청 일괄삭제작업을 마쳤습니다!") })
    }
}