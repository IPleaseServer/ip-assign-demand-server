package site.iplease.iadserver.domain.cancel.subscriber

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.cancel.strategy.DemandCancelCompensateStrategy
import site.iplease.iadserver.domain.cancel.util.DemandCancelErrorOnStatusConverter
import site.iplease.iadserver.global.cancel.data.message.IpAssignDemandCancelErrorOnStatusMessage
import site.iplease.iadserver.global.cancel.subscriber.IpAssignDemandCancelErrorOnStatusSubscriber
import site.iplease.iadserver.infra.alarm.service.PushAlarmService

@Component
class IpAssignDemandCancelErrorOnStatusSubscriberV1(
    private val demandCancelErrorOnStatusConverter: DemandCancelErrorOnStatusConverter,
    private val demandCancelCompensateStrategy: DemandCancelCompensateStrategy,
    private val pushAlarmService: PushAlarmService
): IpAssignDemandCancelErrorOnStatusSubscriber {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun subscribe(message: IpAssignDemandCancelErrorOnStatusMessage) {
        demandCancelErrorOnStatusConverter.convert(message)
            .flatMap { demand -> demandCancelCompensateStrategy.compensate(demand) }
            .flatMap { pushAlarmService.publish(message.issuerId, "IP할당신청을 취소하는중 오류가 발생했어요!", "자세한 내용은 관리자에게 문의해주세요!") }
            .doOnSuccess { logRollback() }
            .doOnError { logError(it) }
            .onErrorResume { Mono.empty() }
            .block()
    }

    private fun logRollback() {
        logger.info("예약 취소에 대한 보상트랜젝션을 성공적으로 수행하였습니다!")
    }

    private fun logError(throwable: Throwable) {
        logger.info("예약 취소에 대한 보상트랜젝션을 수행하던중 오류가 발생하였습니다!")
        logger.info("exception: ${throwable::class.simpleName}")
        logger.info("message: ${throwable.localizedMessage}")
        logger.trace("stacktrace")
        logger.trace(throwable.stackTraceToString())
    }
}