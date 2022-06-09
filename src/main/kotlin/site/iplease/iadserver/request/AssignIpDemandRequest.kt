package site.iplease.iadserver.request

import site.iplease.iadserver.type.AssignIpUsageType
import java.time.LocalDate

data class AssignIpDemandRequest (
    val title: String,
    val description: String,
    val usage: AssignIpUsageType,
    val expireAt: LocalDate,
)
