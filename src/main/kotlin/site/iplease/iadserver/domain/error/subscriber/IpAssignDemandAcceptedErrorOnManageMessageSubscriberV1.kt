package site.iplease.iadserver.domain.error.subscriber

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.error.service.DemandErrorService
import site.iplease.iadserver.domain.error.util.DemandAcceptedErrorOnManageConverter
import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptedErrorOnManageMessage
import site.iplease.iadserver.global.error.subscriber.IpAssignDemandAcceptedErrorOnManageMessageSubscriber

@Component
class IpAssignDemandAcceptedErrorOnManageMessageSubscriberV1(
    private val demandAcceptedErrorOnManageConverter: DemandAcceptedErrorOnManageConverter,
    private val demandErrorService: DemandErrorService
): IpAssignDemandAcceptedErrorOnManageMessageSubscriber {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun subscribe(message: IpAssignDemandAcceptedErrorOnManageMessage) {
        demandAcceptedErrorOnManageConverter.convert(message)
            .flatMap { demand -> demandErrorService.handle(demand) }
            .doOnSuccess { logRollback() }
            .doOnError { logError(it) }
            .onErrorResume { Mono.empty() }
            .block()
    }

    private fun logRollback() {
        logger.info("예약 수락에 대한 보상트랜젝션을 성공적으로 수행하였습니다!")
    }

    private fun logError(throwable: Throwable) {
        logger.info("예약 수락에 대한 보상트랜젝션을 수행하던중 오류가 발생하였습니다!")
        logger.info("exception: ${throwable::class.simpleName}")
        logger.info("message: ${throwable.localizedMessage}")
        logger.trace("stacktrace")
        logger.trace(throwable.stackTraceToString())
    }
}