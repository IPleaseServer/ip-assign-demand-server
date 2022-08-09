package site.iplease.iadserver.global.cancel.subscriber

import site.iplease.iadserver.global.cancel.data.message.IpAssignDemandCancelErrorOnStatusMessage

interface IpAssignDemandCancelErrorOnStatusSubscriber {
    fun subscribe(message: IpAssignDemandCancelErrorOnStatusMessage)
}
