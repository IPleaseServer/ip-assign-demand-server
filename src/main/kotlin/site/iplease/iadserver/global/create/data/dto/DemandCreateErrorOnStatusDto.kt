package site.iplease.iadserver.global.create.data.dto

//IpAssignDemandCreate SequenceDiagram 참조
data class DemandCreateErrorOnStatusDto (
    val demandId: Long,
    val issuerId: Long,
    val message: String
)