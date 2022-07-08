package site.iplease.iadserver.global.error.subscriber

import site.iplease.iadserver.global.error.data.message.IpAssignDemandCancelErrorOnStatusMessage

interface IpAssignDemandCancelErrorOnStatusSubscriber {
    fun subscribe(message: IpAssignDemandCancelErrorOnStatusMessage)
}
