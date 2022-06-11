package site.iplease.iadserver.domain.demand.data.request

import site.iplease.iadserver.domain.demand.data.type.AssignIpUsageType
import java.time.LocalDate

data class AssignIpDemandRequest (
    val title: String,
    val description: String,
    val usage: AssignIpUsageType,
    val expireAt: LocalDate,
)
