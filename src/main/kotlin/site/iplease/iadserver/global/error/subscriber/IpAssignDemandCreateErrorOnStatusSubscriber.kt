package site.iplease.iadserver.global.error.subscriber

import site.iplease.iadserver.global.error.data.message.IpAssignDemandCreateErrorOnStatusMessage

interface IpAssignDemandCreateErrorOnStatusSubscriber {
    fun subscribe(message: IpAssignDemandCreateErrorOnStatusMessage)
}