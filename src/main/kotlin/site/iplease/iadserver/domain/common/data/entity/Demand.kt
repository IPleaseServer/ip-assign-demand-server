package site.iplease.iadserver.domain.common.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import site.iplease.iadserver.global.common.data.type.AssignIpUsageType
import java.time.LocalDate

@Table
data class Demand (
    @Id val idx: Long = 0,
    val identifier: Long,
    val issuerId: Long,
    val title: String,
    val description: String,
    @Column("usage_") val usage: AssignIpUsageType,
    val expireAt: LocalDate
)