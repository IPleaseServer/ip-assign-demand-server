package site.iplease.iadserver.global.demand.subscriber

import site.iplease.iadserver.global.demand.data.message.IpAssignDemandErrorOnStatusMessage

interface IpAssignDemandErrorOnStatusSubscriber {
    fun subscribe(message: IpAssignDemandErrorOnStatusMessage)
}