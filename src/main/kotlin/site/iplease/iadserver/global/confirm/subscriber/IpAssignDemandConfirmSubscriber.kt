package site.iplease.iadserver.global.confirm.subscriber

import site.iplease.iadserver.global.confirm.data.message.IpAssignDemandConfirmMessage

interface IpAssignDemandConfirmSubscriber {
    fun subscribe(message: IpAssignDemandConfirmMessage)
}