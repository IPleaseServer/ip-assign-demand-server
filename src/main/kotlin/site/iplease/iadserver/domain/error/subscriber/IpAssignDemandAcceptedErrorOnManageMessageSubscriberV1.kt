package site.iplease.iadserver.domain.error.subscriber

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.error.service.DemandErrorService
import site.iplease.iadserver.domain.error.util.DemandAcceptedErrorOnManageConverter
import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptedErrorOnManageMessage
import site.iplease.iadserver.global.error.subscriber.IpAssignDemandAcceptedErrorOnManageMessageSubscriber

//- IP할당신청
//demandId에 대한 AssignIpDemand가 존재하지 않을경우 AssignIpDemand를 추가한다.
//삭제 대기열에 demandId에 대한 AssignIpDemand가 존재할 경우 이를 제거한다.
@Component
class IpAssignDemandAcceptedErrorOnManageMessageSubscriberV1(
    private val demandAcceptedErrorOnManageConverter: DemandAcceptedErrorOnManageConverter,
    private val demandErrorService: DemandErrorService
): IpAssignDemandAcceptedErrorOnManageMessageSubscriber {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /*

        demandCancelErrorOnStatusConverter.convert(message)
            .flatMap { demand -> demandErrorService.handle(demand) }
            .doOnSuccess { logRollback() }
            .doOnError { logError(it) }
            .onErrorResume { Mono.empty() }
            .block()
     */
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