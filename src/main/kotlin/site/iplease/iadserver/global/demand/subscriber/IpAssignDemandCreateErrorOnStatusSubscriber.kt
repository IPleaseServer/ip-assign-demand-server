package site.iplease.iadserver.global.demand.subscriber

import site.iplease.iadserver.global.demand.data.message.IpAssignDemandCreateErrorOnStatusMessage

interface IpAssignDemandCreateErrorOnStatusSubscriber {
    fun subscribe(message: IpAssignDemandCreateErrorOnStatusMessage)
}