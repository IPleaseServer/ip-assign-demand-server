package site.iplease.iadserver.global.demand.data.message

data class IpAssignDemandCreateErrorOnStatusMessage(
    val demandId: Long,
    val issuerId: Long,
    val message: String
)
