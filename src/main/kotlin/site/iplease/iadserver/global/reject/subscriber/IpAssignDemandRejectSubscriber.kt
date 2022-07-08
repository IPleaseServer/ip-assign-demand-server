package site.iplease.iadserver.global.reject.subscriber

import site.iplease.iadserver.global.reject.data.message.IpAssignDemandRejectMessage

interface IpAssignDemandRejectSubscriber {
    fun subscribe(message: IpAssignDemandRejectMessage)
}