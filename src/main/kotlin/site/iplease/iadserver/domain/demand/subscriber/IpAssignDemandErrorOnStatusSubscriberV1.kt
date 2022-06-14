package site.iplease.iadserver.domain.demand.subscriber

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.service.DemandErrorService
import site.iplease.iadserver.domain.demand.util.DemandErrorOnStatusConverter
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandErrorOnStatusMessage
import site.iplease.iadserver.global.demand.subscriber.IpAssignDemandErrorOnStatusSubscriber

@Component
class IpAssignDemandErrorOnStatusSubscriberV1(
    private val demandErrorOnStatusConverter: DemandErrorOnStatusConverter,
    private val demandErrorService: DemandErrorService
): IpAssignDemandErrorOnStatusSubscriber {
    override fun subscribe(message: IpAssignDemandErrorOnStatusMessage) {
        demandErrorOnStatusConverter.convert(message)
            .flatMap { demand -> demandErrorService.errorOnStatus(demand) }
            .onErrorResume { Mono.empty() }
            .block()
    }
}