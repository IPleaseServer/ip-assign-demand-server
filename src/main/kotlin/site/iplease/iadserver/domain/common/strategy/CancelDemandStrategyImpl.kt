package site.iplease.iadserver.domain.common.strategy

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.global.common.util.DemandConverter

@Component
class CancelDemandStrategyImpl(
    private val demandRepository: DemandRepository,
    private val demandConverter: DemandConverter
): CancelDemandStrategy {
    override fun cancelDemand(demandId: Long): Mono<DemandDto> =
        demandRepository.findByIdentifier(demandId) //DataStore에서 신청을 조회한다.
            .flatMap { entity -> demandConverter.toDto(entity) } //향후 반환값을 위해 조회한 신청을 Dto로 치환한다.
            .flatMap { dto -> demandRepository.deleteByIdentifier(demandId).then(dto.toMono()) } //신청을 제거하고, 미리 구성해둔 반환값을 발행한다.
}