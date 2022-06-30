package site.iplease.iadserver.global.demand.data.message

import site.iplease.iadserver.global.demand.data.type.DemandStatusType

class IpAssignDemandRejectErrorOnStatusMessage(
    val demandId: Long,
    val issuerId: Long,
    val originStatus: DemandStatusType,
    val message: String
)
