package site.iplease.iadserver.domain.demand.data.dto

//IpAssignDemandCreate SequenceDiagram 참조
data class IpAssignDemandErrorOnStatusDto (
    val demandId: Long,
    val message: String
)