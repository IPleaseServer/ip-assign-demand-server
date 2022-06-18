package site.iplease.iadserver.global.demand.data.message

import site.iplease.iadserver.global.demand.data.type.DemandStatusType

data class IpAssignDemandRejectMessage(
    val demandId: Long,
    val issuerId: Long,
    val originStatus: DemandStatusType,
    val reason: String
)
