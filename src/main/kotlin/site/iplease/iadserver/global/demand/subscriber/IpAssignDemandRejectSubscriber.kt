package site.iplease.iadserver.global.demand.subscriber

import site.iplease.iadserver.global.demand.data.message.IpAssignDemandRejectMessage

interface IpAssignDemandRejectSubscriber {
    fun subscribe(message: IpAssignDemandRejectMessage)
}