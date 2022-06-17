package site.iplease.iadserver.domain.demand.subscriber

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.service.DemandQueryService
import site.iplease.iadserver.domain.demand.util.DemandConverter
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandConfirmMessage
import site.iplease.iadserver.global.demand.subscriber.IpAssignDemandConfirmSubscriber
import site.iplease.iadserver.infra.alarm.service.PushAlarmService

@Component
class IpAssignDemandConfirmSubscriberV1(
    private val demandConverter: DemandConverter,
    private val demandQueryService: DemandQueryService,
    private val pushAlarmService: PushAlarmService
): IpAssignDemandConfirmSubscriber {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun subscribe(message: IpAssignDemandConfirmMessage) {
        demandConverter.toDto(message)//메세지를 DemandDto로 변환한다.
            .flatMap { demand -> demandQueryService.getDemandById(demand.id) }//DemanDto로 예약정보를 가져온다.
            .flatMap { demand -> pushAlarmService.publish(demand.issuerId, "관리자가 신청을 확인했어요!", "곧 좋은소식으로 찾아뵐 수 있도록 노력할게요!") }//푸시알림서비스로 예약신청자에게 에약 확인됨 알림을 보낸다.
            .doOnError() { throwable ->
                logger.error("IP할당신청의 상태를 확인됨으로 전환하던 도중 오류가 발생했습니다!")
                logger.error("exception: ${throwable::class.simpleName}")
                logger.error("message: ${throwable.localizedMessage}")
                logger.trace("stack trace")
                logger.trace(throwable.stackTraceToString())
            }.onErrorResume { Unit.toMono() }
            .block()
    }
}