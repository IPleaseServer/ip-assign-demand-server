package site.iplease.iadserver.domain.demand.legacy.data.dto

//IpAssignDemandCreate SequenceDiagram 참조
data class DemandCreateErrorOnStatusDto (
    val demandId: Long,
    val issuerId: Long,
    val message: String
)