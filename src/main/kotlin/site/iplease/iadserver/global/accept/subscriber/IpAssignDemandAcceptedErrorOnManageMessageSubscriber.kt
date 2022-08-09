package site.iplease.iadserver.global.accept.subscriber

import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptedErrorOnManageMessage

interface IpAssignDemandAcceptedErrorOnManageMessageSubscriber {
    fun subscribe(message: IpAssignDemandAcceptedErrorOnManageMessage)
}
