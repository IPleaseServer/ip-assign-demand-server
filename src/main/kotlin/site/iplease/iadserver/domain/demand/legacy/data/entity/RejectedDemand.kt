package site.iplease.iadserver.domain.demand.legacy.data.entity

data class RejectedDemand(
    val demandId: Long,
    val reason: String,
)