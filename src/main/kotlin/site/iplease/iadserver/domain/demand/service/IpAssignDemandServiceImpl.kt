package site.iplease.iadserver.domain.demand.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.demand.data.dto.DemandDto
import site.iplease.iadserver.domain.demand.repository.DemandRepository
import site.iplease.iadserver.domain.demand.repository.DemandSaver
import site.iplease.iadserver.domain.demand.util.DemandConverter

@Service
@Qualifier("impl")
class IpAssignDemandServiceImpl(
    private val demandRepository: DemandRepository,
    private val demandSaver: DemandSaver,
    private val demandConverter: DemandConverter
): IpAssignDemandService {
    //신청을 추가한다.
    override fun addDemand(demand: DemandDto): Mono<DemandDto> =
        demandConverter.toEntity(demand) //converter를 통해, 파라미터로 받은 dto를 entity로 치환한다.
            .map { entity -> entity.copy(identifier = 0) } //새로운 신청을 추가해야하므로, id를 0으로 설정한다.
            .flatMap { entity -> demandSaver.saveDemand(entity) } //구성한 entity를 repository에 저장한다.
            .flatMap { entity -> demandConverter.toDto(entity) } //저장한 entity를 dto로 치환한다.

    override fun cancelDemand(demandId: Long): Mono<DemandDto> =
        demandRepository.findByIdentifier(demandId) //DataStore에서 신청을 조회한다.
            .flatMap { entity -> demandConverter.toDto(entity) } //향후 반환값을 위해 조회한 신청을 Dto로 치환한다.
            .flatMap { dto -> demandRepository.deleteByIdentifier(demandId).then(dto.toMono()) } //신청을 제거하고, 미리 구성해둔 반환값을 발행한다.

    override fun rejectDemand(demandId: Long, reason: String): Mono<DemandDto> =
        demandRepository.findByIdentifier(demandId)//DataStore에서 신청을 조회한다.
            .flatMap { entity -> demandConverter.toDto(entity) }//향후 반환값을 위해 조회한 신청을 Dto로 치환한다.
            .flatMap { dto -> demandRepository.deleteByIdentifier(demandId).then(dto.toMono()) }//신청을 제거하고, 미리 구성해둔 반환값을 발행한다.

    override fun acceptDemand(demandId: Long, assignIp: String): Mono<DemandDto> =
        demandRepository.findByIdentifier(demandId)//DataStore에서 신청을 조회한다.
            .flatMap { entity -> demandConverter.toDto(entity) }//향후 반환값을 위해 조회한 신청을 Dto로 치환한다
            .flatMap { dto -> demandRepository.deleteByIdentifier(demandId).then(dto.toMono()) }//신청을 제거하고, 미리 구성해둔 반환값을 발행한다.
}