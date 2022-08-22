package site.iplease.iadserver.domain.accept.strategy

import TestDummyDataUtil
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.accept.repository.AcceptedDemandRepository
import site.iplease.iadserver.domain.common.data.entity.Demand
import site.iplease.iadserver.global.accept.data.dto.DemandAcceptedErrorOnManageDto
import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptErrorOnDemandMessage
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.global.common.repository.DemandSaver
import site.iplease.iadserver.global.common.util.DateUtil
import site.iplease.iadserver.global.common.util.DemandConverter
import site.iplease.iadserver.infra.message.service.MessagePublishService
import site.iplease.iadserver.infra.message.type.MessageType

class DemandAcceptCompensateStrategyImplTest {
    private lateinit var demandRepository: DemandRepository
    private lateinit var demandConverter: DemandConverter
    private lateinit var demandSaver: DemandSaver
    private lateinit var acceptedDemandRepository: AcceptedDemandRepository
    private lateinit var messagePublishService: MessagePublishService
    private lateinit var dateUtil: DateUtil
    private lateinit var target: DemandAcceptCompensateStrategyImpl

    @BeforeEach
    fun beforeEach() {
        demandRepository = mock()
        demandConverter = mock()
        demandSaver = mock()
        acceptedDemandRepository = mock()
        messagePublishService = mock()
        dateUtil = mock()
        target = DemandAcceptCompensateStrategyImpl(
            demandRepository,
            demandConverter,
            demandSaver,
            acceptedDemandRepository,
            dateUtil
        )
    }

    //compensate 로직
    //신청 엔티티를 추출한다. - demandConverter
    //신청 엔티티를 저장한다. - demandSaver
    //수락된신청 엔티티를 삭제한다. - demandRepository
    //IP할당신청수락오류 메세지를 구성한다.
    //IP할당신청수락오류 메세지를 발행한다.

    @Test @DisplayName("IP할당신청수락됨오류처리 성공 테스트")
    fun testCompensateSuccess() {
        //오류처리에 성공하였다면, Mono<Unit>을 반환한다.
        //오류처리가 성공하려면 아래의 작업이 성공해야한다.
        //- 신청복구 트랜잭션 수행 (신청 엔티티 저장)
        //- 신청수락 취소 트랜잭션 수행 (수락된신청 엔티티 삭제)

        //given
        val demandId = TestDummyDataUtil.id()
        val demandIssuerId = TestDummyDataUtil.id()
        val assignIp = TestDummyDataUtil.assignIp()
        val originStatus = TestDummyDataUtil.demandStatus()
        val message = TestDummyDataUtil.errorMessage()

        val dto = mock<DemandAcceptedErrorOnManageDto>()
        val entity = mock<Demand>()
        val savedEntity = mock<Demand>()
        val errorMessage = IpAssignDemandAcceptErrorOnDemandMessage(demandIssuerId, demandId, assignIp, originStatus, message)
        val loggingDate = TestDummyDataUtil.randomDate()

        //when
        whenever(dto.demandId).thenReturn(demandId)
        whenever(dto.demandIssuerId).thenReturn(demandIssuerId)
        whenever(dto.assignIp).thenReturn(assignIp)
        whenever(dto.originStatus).thenReturn(originStatus)
        whenever(dto.message).thenReturn(message)

        whenever(demandRepository.existsByIdentifier(demandId)).thenReturn(false.toMono())
        whenever(demandConverter.toEntity(dto)).thenReturn(entity.toMono())
        whenever(demandSaver.saveDemand(entity, demandId)).thenReturn(savedEntity.toMono())
        whenever(acceptedDemandRepository.delete(demandId)).thenReturn(demandId.toMono())
        whenever(dateUtil.dateNow()).thenReturn(loggingDate)
        whenever(messagePublishService.publish(MessageType.IP_ASSIGN_DEMAND_ACCEPT_ERROR_ON_DEMAND, errorMessage)).thenReturn(Unit.toMono())


        target.compensate(dto).block()!!

        //then
        verify(demandSaver, times(1)).saveDemand(entity, demandId)
        verify(acceptedDemandRepository, times(1)).delete(demandId)
        verify(messagePublishService, times(1)).publish(MessageType.IP_ASSIGN_DEMAND_ACCEPT_ERROR_ON_DEMAND, errorMessage)
    }
}