package site.iplease.iadserver.domain.demand.legacy.data.dto

import site.iplease.iadserver.domain.demand.legacy.data.type.AssignIpUsageType
import java.time.LocalDate

data class DemandCancelErrorOnStatusDto (
    val demandId: Long,
    val issuerId: Long,
    val title: String,
    val description: String,
    val usage: AssignIpUsageType,
    val expireAt: LocalDate,
    val message: String
)
