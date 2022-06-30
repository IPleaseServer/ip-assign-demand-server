package site.iplease.iadserver.global.demand.subscriber

import site.iplease.iadserver.global.demand.data.message.IpAssignDemandCreateErrorOnStatusMessage

//TODO IpAssignDemand"Create"ErrorOnStatusSubscriber로 이름변경
interface IpAssignDemandCreateErrorOnStatusSubscriber {
    fun subscribe(message: IpAssignDemandCreateErrorOnStatusMessage)
}