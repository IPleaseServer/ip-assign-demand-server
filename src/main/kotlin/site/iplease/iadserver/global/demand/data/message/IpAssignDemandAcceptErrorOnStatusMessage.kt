package site.iplease.iadserver.global.demand.data.message

import site.iplease.iadserver.global.demand.data.type.DemandStatusType

data class IpAssignDemandAcceptErrorOnStatusMessage (
    val issuerId: Long,
    val demandId: Long,
    val assignIp: String,
    val originStatus: DemandStatusType
)