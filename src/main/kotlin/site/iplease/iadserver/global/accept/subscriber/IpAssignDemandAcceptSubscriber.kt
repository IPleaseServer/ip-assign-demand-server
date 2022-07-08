package site.iplease.iadserver.global.accept.subscriber

import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptMessage

interface IpAssignDemandAcceptSubscriber {
    fun subscribe(message: IpAssignDemandAcceptMessage)
}