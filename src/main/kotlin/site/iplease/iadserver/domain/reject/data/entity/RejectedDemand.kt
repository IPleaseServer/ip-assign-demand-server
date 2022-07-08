package site.iplease.iadserver.domain.reject.data.entity

data class RejectedDemand(
    val demandId: Long,
    val reason: String,
)