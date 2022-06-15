package site.iplease.iadserver.global.demand.subscriber

import site.iplease.iadserver.global.demand.data.message.IpAssignDemandErrorOnStatusMessage

//TODO IpAssignDemand"Create"ErrorOnStatusSubscriber로 이름변경
interface IpAssignDemandErrorOnStatusSubscriber {
    fun subscribe(message: IpAssignDemandErrorOnStatusMessage)
}