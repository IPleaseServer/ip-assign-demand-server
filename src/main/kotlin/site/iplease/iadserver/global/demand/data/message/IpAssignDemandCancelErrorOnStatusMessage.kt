package site.iplease.iadserver.global.demand.data.message

import site.iplease.iadserver.domain.demand.data.type.AssignIpUsageType
import java.time.LocalDate

data class IpAssignDemandCancelErrorOnStatusMessage(
    val id: Long,
    val issuerId: Long,
    val title: String,
    val description: String,
    val usage: AssignIpUsageType,
    val expireAt: LocalDate,
    val message: String
)