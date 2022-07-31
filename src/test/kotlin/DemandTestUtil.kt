import site.iplease.iadserver.domain.common.data.entity.Demand
import site.iplease.iadserver.global.common.data.dto.DemandDto

object DemandTestUtil {
    fun toEntity(idx: Long, dto: DemandDto): Demand =
        Demand(idx, dto.id, dto.issuerId, dto.title, dto.description, dto.usage, dto.expireAt)

}
