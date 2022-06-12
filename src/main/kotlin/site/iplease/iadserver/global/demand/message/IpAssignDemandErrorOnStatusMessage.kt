package site.iplease.iadserver.global.demand.message

data class IpAssignDemandErrorOnStatusMessage(
    val demandId: Long,
    val message: String
)
