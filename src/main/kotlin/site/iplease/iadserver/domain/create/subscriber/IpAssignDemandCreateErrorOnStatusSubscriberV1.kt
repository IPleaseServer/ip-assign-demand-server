package site.iplease.iadserver.domain.create.subscriber

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.create.strategy.DemandCreateCompensateStrategy
import site.iplease.iadserver.domain.create.util.DemandCreateErrorOnStatusConverter
import site.iplease.iadserver.global.create.data.message.IpAssignDemandCreateErrorOnStatusMessage
import site.iplease.iadserver.global.create.subscriber.IpAssignDemandCreateErrorOnStatusSubscriber
import site.iplease.iadserver.infra.alarm.service.PushAlarmService

@Component
class IpAssignDemandCreateErrorOnStatusSubscriberV1(
    private val demandCreateErrorOnStatusConverter: DemandCreateErrorOnStatusConverter,
    private val demandCreateCompensateStrategy: DemandCreateCompensateStrategy,
    private val pushAlarmService: PushAlarmService
): IpAssignDemandCreateErrorOnStatusSubscriber {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun subscribe(message: IpAssignDemandCreateErrorOnStatusMessage) {
        demandCreateErrorOnStatusConverter.convert(message)
            .flatMap { demand -> demandCreateCompensateStrategy.compensate(demand) }
            .flatMap { pushAlarmService.publish(message.issuerId, "IP할당신청을 등록하는중 오류가 발생했어요!", "자세한 내용은 관리자에게 문의해주세요!") }
            .doOnSuccess { logRollback() }
            .doOnError { logError(it) }
            .onErrorResume { Mono.empty() }
            .block()
    }

    private fun logRollback() {
        logger.info("신청 추가에 대한 보상트랜젝션을 성공적으로 수행하였습니다!")
    }

    private fun logError(throwable: Throwable) {
        logger.info("신청 추가에 대한 보상트랜젝션을 수행하던중 오류가 발생하였습니다!")
        logger.info("exception: ${throwable::class.simpleName}")
        logger.info("message: ${throwable.localizedMessage}")
        logger.trace("stacktrace")
        logger.trace(throwable.stackTraceToString())
    }
}