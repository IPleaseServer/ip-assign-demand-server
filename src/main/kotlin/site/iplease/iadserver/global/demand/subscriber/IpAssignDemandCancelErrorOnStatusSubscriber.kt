package site.iplease.iadserver.global.demand.subscriber

import site.iplease.iadserver.global.demand.data.message.IpAssignDemandCancelErrorOnStatusMessage

interface IpAssignDemandCancelErrorOnStatusSubscriber {
    fun subscribe(message: IpAssignDemandCancelErrorOnStatusMessage)
}
