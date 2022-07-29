package site.iplease.iadserver.domain.common.data.request

import site.iplease.iadserver.global.common.data.type.AssignIpUsageType
import java.time.LocalDate

data class CreateAssignIpDemandRequest (
    val title: String,
    val description: String,
    val usage: AssignIpUsageType,
    val expireAt: LocalDate,
)
