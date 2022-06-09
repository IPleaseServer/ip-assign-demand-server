package site.iplease.iadserver.data.entity

import site.iplease.iadserver.data.type.AssignIpUsageType
import java.time.LocalDate

data class Demand (
    val id: Long = 0,
    val issuerId: Long,
    val title: String,
    val description: String,
    val usage: AssignIpUsageType,
    val expireAt: LocalDate
)