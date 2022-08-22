package site.iplease.iadserver.domain.accept.subscriber

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.accept.strategy.DemandAcceptCompensateStrategy
import site.iplease.iadserver.domain.accept.util.DemandAcceptedErrorOnManageConverter
import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptErrorOnDemandMessage
import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptedErrorOnManageMessage
import site.iplease.iadserver.global.accept.subscriber.IpAssignDemandAcceptedErrorOnManageMessageSubscriber
import site.iplease.iadserver.infra.message.service.MessagePublishService
import site.iplease.iadserver.infra.message.type.MessageType

@Component
class IpAssignDemandAcceptedErrorOnManageMessageSubscriberV1(
    private val demandAcceptedErrorOnManageConverter: DemandAcceptedErrorOnManageConverter,
    private val demandAcceptCompensateStrategy: DemandAcceptCompensateStrategy,
    private val messagePublishService: MessagePublishService
): IpAssignDemandAcceptedErrorOnManageMessageSubscriber {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun subscribe(message: IpAssignDemandAcceptedErrorOnManageMessage) {
        demandAcceptedErrorOnManageConverter.convert(message)
            .flatMap { demand -> demandAcceptCompensateStrategy.compensate(demand).map { demand } }
            .flatMap { demand -> messagePublishService.publish(
                MessageType.IP_ASSIGN_DEMAND_ACCEPT_ERROR_ON_DEMAND,
                IpAssignDemandAcceptErrorOnDemandMessage(demand.demandIssuerId, demand.demandId, demand.assignIp, demand.originStatus, demand.message)
            )}
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