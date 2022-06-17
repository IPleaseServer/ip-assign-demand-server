package site.iplease.iadserver.global.demand.subscriber

import site.iplease.iadserver.global.demand.data.message.IpAssignDemandConfirmMessage

interface IpAssignDemandConfirmSubscriber {
    fun subscribe(message: IpAssignDemandConfirmMessage)
}