package site.iplease.iadserver.domain.reject.subscriber

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.reject.exception.IpAssignDemandRejectFailureException
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.service.IpAssignDemandService
import site.iplease.iadserver.global.reject.data.message.IpAssignDemandRejectErrorOnDemandMessage
import site.iplease.iadserver.global.reject.data.message.IpAssignDemandRejectMessage
import site.iplease.iadserver.global.reject.subscriber.IpAssignDemandRejectSubscriber
import site.iplease.iadserver.infra.alarm.data.type.AlarmType
import site.iplease.iadserver.infra.alarm.service.PushAlarmService
import site.iplease.iadserver.infra.message.service.MessagePublishService
import site.iplease.iadserver.infra.message.type.MessageType

@Component
class IpAssignDemandRejectSubscriberV1(
    private val demandService: IpAssignDemandService,
    private val pushAlarmService: PushAlarmService,
    private val messagePublishService: MessagePublishService
): IpAssignDemandRejectSubscriber {
    override fun subscribe(message: IpAssignDemandRejectMessage) {
        //메세지에서 demandId를 가져와, 예약을 거절시킨다.
        demandService.rejectDemand(message.demandId, message.reason)
            .flatMap { demand -> sendStudentAlarm(demand, message) }
            .onErrorResume { throwable -> Mono.error(IpAssignDemandRejectFailureException(throwable)) }//로직 처리중, 오류가 발생하면, 해당 오류를 Wrapping한다.
            .flatMap { _ -> pushAlarmService.publish(message.issuerId, "신청 거절에 성공했어요!", "선생님의 메세지를 거절사유에 담아 학생에게 전달하고있어요.") }//교사에게 거절성공 알림을 보낸다.
            .onErrorResume(IpAssignDemandRejectFailureException::class.java) { throwable ->
                val errorMessage = IpAssignDemandRejectErrorOnDemandMessage(message.demandId, message.issuerId, message.originStatus, throwable.localizedMessage)
                messagePublishService.publish(MessageType.IP_ASSIGN_DEMAND_REJECT_ERROR_ON_DEMAND, errorMessage)//Wrapping했던 오류에 대한 처리(오류 메세지 전파)를 진행한다.
            }.block()
    }

    private fun sendStudentAlarm(demand: DemandDto, message: IpAssignDemandRejectMessage) =
        pushAlarmService.publish(demand.issuerId, "IP할당 신청이 거절됬어요!", "자세한 내용은 이메일을 통해 확인해주세요.")
            .flatMap {
                pushAlarmService.publish(demand.issuerId, "IP할당 신청이 거절됬어요!", """
                    IP할당신청이 거절됬어요 어떤 할당신청인지 알아볼까요?
        
                    > 거절된 신청 제목: ${demand.title}
                    > 
                    > 거절된 신청 설명: ${demand.description}
                    > 
                    > 거절한 관리자: 
                    > 
                    > 거절사유: ${message.reason}
                    """.trimIndent(), AlarmType.EMAIL
                )//학생에게 신청거절됨 알림을 보낸다.
            }.then(demand.toMono())
}