package site.iplease.iadserver.domain.demand.legacy.subscriber

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.legacy.service.DemandErrorService
import site.iplease.iadserver.domain.demand.legacy.util.DemandCreateErrorOnStatusConverter
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandCreateErrorOnStatusMessage
import site.iplease.iadserver.global.demand.subscriber.IpAssignDemandCreateErrorOnStatusSubscriber

@Component
class IpAssignDemandCreateErrorOnStatusSubscriberV1(
    private val demandCreateErrorOnStatusConverter: DemandCreateErrorOnStatusConverter,
    private val demandErrorService: DemandErrorService
): IpAssignDemandCreateErrorOnStatusSubscriber {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun subscribe(message: IpAssignDemandCreateErrorOnStatusMessage) {
        demandCreateErrorOnStatusConverter.convert(message)
            .flatMap { demand -> demandErrorService.handle(demand) }
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