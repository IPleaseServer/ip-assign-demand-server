package site.iplease.iadserver.global.reject.data.message

import site.iplease.iadserver.global.common.data.type.DemandStatusType

data class IpAssignDemandRejectMessage(
    val demandId: Long,
    val issuerId: Long,
    val originStatus: DemandStatusType,
    val reason: String
)
