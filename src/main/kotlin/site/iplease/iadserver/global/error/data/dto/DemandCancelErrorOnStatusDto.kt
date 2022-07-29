package site.iplease.iadserver.global.error.data.dto

import site.iplease.iadserver.global.common.data.type.AssignIpUsageType
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
