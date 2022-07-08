package site.iplease.iadserver.domain.common.strategy

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.repository.DemandSaver
import site.iplease.iadserver.global.common.util.DemandConverter

@Component
class AddDemandStrategyImpl(
    private val demandSaver: DemandSaver,
    private val demandConverter: DemandConverter
): AddDemandStrategy {
    override fun addDemand(demand: DemandDto): Mono<DemandDto> =
        demandConverter.toEntity(demand) //converter를 통해, 파라미터로 받은 dto를 entity로 치환한다.
            .map { entity -> entity.copy(identifier = 0) } //새로운 신청을 추가해야하므로, id를 0으로 설정한다.
            .flatMap { entity -> demandSaver.saveDemand(entity) } //구성한 entity를 repository에 저장한다.
            .flatMap { entity -> demandConverter.toDto(entity) } //저장한 entity를 dto로 치환한다.
}