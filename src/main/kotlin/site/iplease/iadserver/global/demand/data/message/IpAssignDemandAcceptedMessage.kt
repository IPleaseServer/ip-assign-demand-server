package site.iplease.iadserver.global.demand.data.message

import site.iplease.iadserver.domain.demand.data.type.AssignIpUsageType
import site.iplease.iadserver.global.demand.data.type.DemandStatusType
import java.time.LocalDate

data class IpAssignDemandAcceptedMessage (
    val assignIp: String,
    //Push Alarm Data
    val issuerId: Long,
    //Demand Status Rollback Data
    val originStatus: DemandStatusType,
    //Demand Rollback Data
    val demandId: Long,
    val demandIssuerId: Long,
    val title: String,
    val description: String,
    val usage: AssignIpUsageType,
    val expireAt: LocalDate
)
