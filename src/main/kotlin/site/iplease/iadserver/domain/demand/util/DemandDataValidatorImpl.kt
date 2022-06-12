package site.iplease.iadserver.domain.demand.util

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.exception.WrongExpireDateException
import site.iplease.iadserver.domain.demand.exception.WrongTitleException
import site.iplease.iadserver.global.common.util.DateUtil

@Component
class DemandDataValidatorImpl(
    private val dateUtil: DateUtil
): DemandDataValidator {
    override fun validate(dto: DemandDto): Mono<DemandDto> =
        dto.toMono()
            .flatMap { demand -> checkExpireAt(demand) }
            .flatMap { demand -> checkTitle(demand)}

    private fun checkExpireAt(demand: DemandDto) =
        if(demand.expireAt.isAfter(dateUtil.dateNow())) demand.toMono()
        else Mono.error(WrongExpireDateException("만료일은 오늘 이후여야합니다!", demand.expireAt))

    private fun checkTitle(demand: DemandDto) =
        if(demand.title.length <= 25) demand.toMono()
        else Mono.error(WrongTitleException("신청 제목은 25자 이하여야합니다!"))
}