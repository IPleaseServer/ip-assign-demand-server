package site.iplease.iadserver.global.demand.data.message

import site.iplease.iadserver.global.demand.data.type.DemandStatusType

class IpAssignDemandRejectErrorOnStatusMessage(
    val demandId: Long,
    val issuerId: Long,
    val originStatus: DemandStatusType,//TODO 에러 메세지 관련 프로퍼티 추가
)
