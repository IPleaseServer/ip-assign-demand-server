package site.iplease.iadserver.domain.demand.data.entity

data class RejectedDemand(
    val demandId: Long,
    val reason: String,
)