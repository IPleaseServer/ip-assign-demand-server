package site.iplease.iadserver.global.demand.data.message

data class IpAssignDemandErrorOnStatusMessage(
    val demandId: Long,
    val issuerId: Long,
    val message: String
)
