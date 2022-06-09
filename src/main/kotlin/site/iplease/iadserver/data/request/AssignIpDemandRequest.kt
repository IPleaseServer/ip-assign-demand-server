package site.iplease.iadserver.data.request

import site.iplease.iadserver.data.type.AssignIpUsageType
import java.time.LocalDate

data class AssignIpDemandRequest (
    val title: String,
    val description: String,
    val usage: AssignIpUsageType,
    val expireAt: LocalDate,
)
