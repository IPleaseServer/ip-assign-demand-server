package site.iplease.iadserver.global.create.subscriber

import site.iplease.iadserver.global.create.data.message.IpAssignDemandCreateErrorOnStatusMessage

interface IpAssignDemandCreateErrorOnStatusSubscriber {
    fun subscribe(message: IpAssignDemandCreateErrorOnStatusMessage)
}