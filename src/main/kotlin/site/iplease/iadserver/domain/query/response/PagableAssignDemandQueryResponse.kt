package site.iplease.iadserver.domain.query.response

import org.springframework.data.domain.Page
import site.iplease.iadserver.global.common.data.dto.DemandDto

data class PagableAssignDemandQueryResponse(
    val data: Page<DemandDto>
)
