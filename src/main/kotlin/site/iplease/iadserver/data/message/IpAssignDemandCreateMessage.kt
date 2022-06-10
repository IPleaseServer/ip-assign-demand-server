package site.iplease.iadserver.data.message

data class IpAssignDemandCreateMessage (
    val demandId: Long,
    val issuerId: Long
)
