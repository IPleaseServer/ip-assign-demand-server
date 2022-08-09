package site.iplease.iadserver.domain.accept.subscriber

import TestDummyDataUtil
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptMessage
import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptedMessage
import site.iplease.iadserver.global.accept.util.AssignIpValidator
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.service.IpAssignDemandService
import site.iplease.iadserver.global.common.util.DemandConverter
import site.iplease.iadserver.infra.message.service.MessagePublishService
import site.iplease.iadserver.infra.message.type.MessageType

class IpAssignDemandAcceptSubscriberV1Test {
    private lateinit var demandService: IpAssignDemandService
    private lateinit var demandConverter: DemandConverter
    private lateinit var messagePublishService: MessagePublishService
    private lateinit var assignIpValidator: AssignIpValidator
    private lateinit var target: IpAssignDemandAcceptSubscriberV1

    @BeforeEach
    fun beforeEach() {
        demandService = mock()
        demandConverter = mock()
        messagePublishService = mock()
        assignIpValidator = mock()
        target = IpAssignDemandAcceptSubscriberV1(demandService, demandConverter, messagePublishService, assignIpValidator)
    }

    //subscribe 로직
    //할당IP정보를 추출하여 검증한다. -> assignIpValidator
    //신청수락 트랜잭션을 수행한다. -> demandService
    //성공시, 할당IP추가 메세지를 구성한다. -> demandConverter
    //성공시, 할당IP추가 메세지를 발행한다. -> messagePublishService
    //실패시, IP할당신청수락오류 메세지를 구성한다.
    //실패시, IP할당신청수락오류 메세지를 발행한다. -> messagePublishService

    @Test @DisplayName("IP할당신청수락 성공 테스트")
    fun testSubscribeSuccess() {
        //신청수락에 성공하였다면, 할당IP추가 메세지를 발행한다.
        //신청수락이 성공하려면 아래의 작업이 성공해야한다.
        //- 신청수락 정책 검증
        //- 신청수락 트랜잭션 수행

        //given
        val issuerId = TestDummyDataUtil.id()
        val demandId = TestDummyDataUtil.id()
        val assignIp = TestDummyDataUtil.assignIp()
        val originStatus = TestDummyDataUtil.demandStatus()
        val acceptMessage = IpAssignDemandAcceptMessage(issuerId, demandId, assignIp, originStatus)
        val acceptedMessage = mock<IpAssignDemandAcceptedMessage>()
        val dto = mock<DemandDto>()

        //when
        whenever(assignIpValidator.validate(acceptMessage.assignIp)).thenReturn(Unit.toMono())
        whenever(demandService.acceptDemand(acceptMessage.demandId, acceptMessage.assignIp)).thenReturn(dto.toMono())
        whenever(demandConverter.toAssignIpCreateMessage(dto, acceptMessage)).thenReturn(acceptedMessage.toMono())
        whenever(messagePublishService.publish(MessageType.IP_ASSIGN_DEMAND_ACCEPTED, acceptedMessage)).thenReturn(Unit.toMono())

        target.subscribe(acceptMessage)

        //then
        verify(messagePublishService, times(1)).publish(MessageType.IP_ASSIGN_DEMAND_ACCEPTED, acceptedMessage)
        verify(messagePublishService, times(0)).publish(eq(MessageType.IP_ASSIGN_DEMAND_ACCEPT_ERROR_ON_DEMAND), any())
    }
}