package site.iplease.iadserver.global.demand.subscriber

import site.iplease.iadserver.global.demand.data.message.IpAssignDemandAcceptMessage

interface IpAssignDemandAcceptSubscriber {
    fun subscribe(message: IpAssignDemandAcceptMessage)
}