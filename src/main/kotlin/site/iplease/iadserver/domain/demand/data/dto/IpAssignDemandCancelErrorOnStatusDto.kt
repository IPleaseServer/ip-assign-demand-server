package site.iplease.iadserver.domain.demand.data.dto

import site.iplease.iadserver.domain.demand.data.type.AssignIpUsageType
import java.time.LocalDate

data class IpAssignDemandCancelErrorOnStatusDto (
    val demandId: Long,
    val issuerId: Long,
    val title: String,
    val description: String,
    val usage: AssignIpUsageType,
    val expireAt: LocalDate,
    val message: String
)
