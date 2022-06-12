package site.iplease.iadserver.global.demand.subscriber

import site.iplease.iadserver.global.demand.message.IpAssignDemandErrorOnStatusMessage

interface IpAssignDemandErrorOnStatusSubscriber {
    fun subscribe(message: IpAssignDemandErrorOnStatusMessage)
}