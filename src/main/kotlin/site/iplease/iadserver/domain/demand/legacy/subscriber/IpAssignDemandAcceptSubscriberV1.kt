package site.iplease.iadserver.domain.demand.legacy.subscriber

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.legacy.exception.IpAssignDemandAcceptFailureException
import site.iplease.iadserver.domain.demand.legacy.service.IpAssignDemandService
import site.iplease.iadserver.domain.demand.legacy.util.AssignIpValidator
import site.iplease.iadserver.domain.demand.legacy.util.DemandConverter
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandAcceptErrorOnDemandMessage
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandAcceptMessage
import site.iplease.iadserver.global.demand.subscriber.IpAssignDemandAcceptSubscriber
import site.iplease.iadserver.infra.message.service.MessagePublishService
import site.iplease.iadserver.infra.message.type.MessageType

@Component
class IpAssignDemandAcceptSubscriberV1(
    @Qualifier("lazyAccept") private val demandService: IpAssignDemandService,
    private val demandConverter: DemandConverter,
    private val messagePublishService: MessagePublishService,
    private val assignIpValidator: AssignIpValidator,
): IpAssignDemandAcceptSubscriber {
    override fun subscribe(message: IpAssignDemandAcceptMessage) {
        assignIpValidator.validate(message.assignIp)
            .flatMap { demandService.acceptDemand(message.demandId, message.assignIp) }
            .onErrorResume { throwable -> Mono.error(IpAssignDemandAcceptFailureException(throwable)) }
            .flatMap { demand -> demandConverter.toAssignIpCreateMessage(demand, message) }
            .flatMap { messagePublishService.publish(MessageType.IP_ASSIGN_DEMAND_ACCEPTED, it) }
            .onErrorResume(IpAssignDemandAcceptFailureException::class.java) { throwable ->
                val errorMessage = IpAssignDemandAcceptErrorOnDemandMessage(message.issuerId, message.demandId, message.assignIp, message.originStatus, throwable.localizedMessage)
                messagePublishService.publish(MessageType.IP_ASSIGN_DEMAND_ACCEPT_ERROR_ON_DEMAND, errorMessage)
            }.block()
    }
}