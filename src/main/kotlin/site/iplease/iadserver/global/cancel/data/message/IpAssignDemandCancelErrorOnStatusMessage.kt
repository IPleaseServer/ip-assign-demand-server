package site.iplease.iadserver.global.cancel.data.message

import site.iplease.iadserver.global.common.data.type.AssignIpUsageType
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