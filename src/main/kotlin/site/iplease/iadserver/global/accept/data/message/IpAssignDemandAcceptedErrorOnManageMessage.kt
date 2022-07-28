package site.iplease.iadserver.global.accept.data.message

import site.iplease.iadserver.global.common.data.type.AssignIpUsageType
import site.iplease.iadserver.global.common.data.type.DemandStatusType
import java.time.LocalDate

data class IpAssignDemandAcceptedErrorOnManageMessage(
    val demandId: Long,
    val originStatus: DemandStatusType,
    val issuerId: Long,
    val demandIssuerId: Long,
    val title: String,
    val description: String,
    val usage: AssignIpUsageType,
    val expireAt: LocalDate,
    val assignIp: String,
    val message: String
)