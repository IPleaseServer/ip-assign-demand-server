package site.iplease.iadserver.domain.error.data.dto

//IpAssignDemandCreate SequenceDiagram 참조
data class DemandCreateErrorOnStatusDto (
    val demandId: Long,
    val issuerId: Long,
    val message: String
)