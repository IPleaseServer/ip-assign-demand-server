package site.iplease.iadserver.domain.error.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.accept.repository.AcceptedDemandRepository
import site.iplease.iadserver.global.error.data.dto.DemandCancelErrorOnStatusDto
import site.iplease.iadserver.global.error.data.dto.DemandCreateErrorOnStatusDto
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.global.common.repository.DemandSaver
import site.iplease.iadserver.global.common.util.DemandConverter
import site.iplease.iadserver.global.common.util.DateUtil
import site.iplease.iadserver.global.error.data.dto.DemandAcceptedErrorOnManageDto
import site.iplease.iadserver.global.error.data.message.IpAssignDemandAcceptErrorOnDemandMessage
import site.iplease.iadserver.infra.alarm.service.PushAlarmService
import site.iplease.iadserver.infra.message.service.MessagePublishService
import site.iplease.iadserver.infra.message.type.MessageType
import kotlin.math.absoluteValue
import kotlin.random.Random

@Service
class DemandErrorServiceImpl(
    private val demandConverter: DemandConverter,
    private val demandRepository: DemandRepository,
    private val acceptedDemandRepository: AcceptedDemandRepository,
    private val demandSaver: DemandSaver,
    private val pushAlarmService: PushAlarmService,
    private val messagePublishService: MessagePublishService,
    private val dateUtil: DateUtil
): DemandErrorService {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun handle(demand: DemandCreateErrorOnStatusDto): Mono<Unit> =
        demandRepository.deleteByIdentifier(demand.demandId)
            .then(Unit.toMono())

            .map { createRandomId() }
            .flatMap { id -> logErrorOnCreate(id, demand) }
            .flatMap { id -> pushAlarmService.publish(demand.issuerId, "할당IP신청중, 오류가 발생했습니다!", "다음 아이디로 관리자에게 문의해주세요!  - $id") }

    override fun handle(demand: DemandCancelErrorOnStatusDto): Mono<Unit> =
        demandConverter.toEntity(demand)
            .flatMap { entity -> demandSaver.saveDemand(entity, entity.identifier) }
            .map { createRandomId() }
            .flatMap { id -> logErrorOnCancel(id, demand) }
            .flatMap { id -> pushAlarmService.publish(demand.issuerId, "할당IP신청취소중, 오류가 발생했습니다!", "다음 아이디로 관리자에게 문의해주세요!  - $id") }

    override fun handle(demand: DemandAcceptedErrorOnManageDto): Mono<Unit> =
    //demandId에 대한 AssignIpDemand가 존재하지 않을경우 AssignIpDemand를 추가한다.
        demandRepository.existsByIdentifier(demand.demandId)
            .flatMap { isExists ->
                if(isExists) Mono.error(RuntimeException())
                else demandConverter.toEntity(demand)
            }.flatMap { demandSaver.saveDemand(it, demand.demandId) }
            .onErrorResume { Mono.empty() }
            //삭제 대기열에 demandId에 대한 AssignIpDemand가 존재할 경우 이를 제거한다.
            .flatMap { acceptedDemandRepository.delete(demand.demandId) }
            .map { createRandomId() }
            .flatMap { id -> logErrorOnAccepted(id, demand) }
            .flatMap { messagePublishService.publish(
                MessageType.IP_ASSIGN_DEMAND_ACCEPT_ERROR_ON_DEMAND,
                IpAssignDemandAcceptErrorOnDemandMessage(demand.demandIssuerId, demand.demandId, demand.assignIp, demand.originStatus, demand.message)
            ) }

    private fun logErrorOnCreate(id: String, demand: DemandCreateErrorOnStatusDto): Mono<String> =
        logErrorOnStatus(id, "할당IP신청중 오류가 발생했습니다!", issuerId = demand.issuerId, demandId = demand.demandId, errorMessage = demand.message)
    private fun logErrorOnAccepted(id: String, demand: DemandAcceptedErrorOnManageDto): Mono<String> =
        logErrorOnStatus(id, "할당IP신청수락중 오류가 발생했습니다!", issuerId = demand.issuerId, demandId = demand.demandId, errorMessage = demand.message)
    private fun logErrorOnCancel(id: String, demand: DemandCancelErrorOnStatusDto): Mono<String> =
        logErrorOnStatus(id, "할당IP신청취소중 오류가 발생했습니다!", issuerId = demand.issuerId, demandId = demand.demandId, errorMessage = demand.message)

    private fun logErrorOnStatus(id: String, description: String, issuerId: Long, demandId: Long, errorMessage: String): Mono<String> =
        id.toMono().map {
                logger.warn("[$id] $description")
                logger.warn("[$id] issuerId: $issuerId")
                logger.warn("[$id] demandId: $demandId")
                logger.warn("[$id] message: $errorMessage")
            }.map { id }

    //가끔 한두번은 겹쳐도 상관없다.
    private fun createRandomId() =  dateUtil.dateNow().dayOfMonth.toString() + Random.nextInt().absoluteValue.toString()
}