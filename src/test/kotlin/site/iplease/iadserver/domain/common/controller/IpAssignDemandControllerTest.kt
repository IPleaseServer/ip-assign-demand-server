package site.iplease.iadserver.domain.common.controller

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.common.data.request.CancelAssignIpDemandRequest
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.data.message.IpAssignDemandCancelMessage
import site.iplease.iadserver.global.common.data.type.DemandPolicyType
import site.iplease.iadserver.global.common.service.IpAssignDemandService
import site.iplease.iadserver.global.common.util.DemandConverter
import site.iplease.iadserver.global.common.util.DemandPolicyValidator
import site.iplease.iadserver.infra.message.service.MessagePublishService
import site.iplease.iadserver.infra.message.type.MessageType
import kotlin.random.Random

class IpAssignDemandControllerTest {
    private lateinit var ipAssignDemandService: IpAssignDemandService
    private lateinit var messagePublishService: MessagePublishService
    private lateinit var demandConverter: DemandConverter
    private lateinit var demandPolicyValidator: DemandPolicyValidator
    private lateinit var target: IpAssignDemandController

    @BeforeEach
    fun beforeEach() {
        ipAssignDemandService = mock(IpAssignDemandService::class.java)
        messagePublishService = mock(MessagePublishService::class.java)
        demandConverter = mock(DemandConverter::class.java)
        demandPolicyValidator = mock(DemandPolicyValidator::class.java)
        target = IpAssignDemandController(ipAssignDemandService, messagePublishService, demandConverter, demandPolicyValidator)
    }

    //cancelAssignIpDemand 로직
    //요청정보를 검증한다. -> demandPolicyValidator
    //예약취소 트랜잭션을 수행한다. -> ipAssignDemandService
    //예약취소됨 메세지를 구성한다. -> demandConverter
    //예약취소됨 메세지를 발행한다. -> messagePublishService
    //반환값을 ResponseEntity로 감싸 반환한다.

    @Test @DisplayName("IP할당예약취소 성공 테스트")
    fun testCancelAssignIpDemandSuccess() {
        //예약취소에 성공하였다면, 200 OK를 반환한다.
        //예약취소가 성공하려면 아래의 작업이 성공해야한다.
        //- 예약취소 정책 검증
        //- 예약취소 트랜잭션 수행
        //- 예약취소됨 메세지 발행

        //given
        val issuerId = Random.nextLong()
        val demandId = Random.nextLong()
        val request = CancelAssignIpDemandRequest(demandId)
        val dto = mock(DemandDto::class.java)
        val message = mock(IpAssignDemandCancelMessage::class.java)

        //when
        whenever(demandPolicyValidator.validate(any(), eq(DemandPolicyType.DEMAND_CANCEL))).thenReturn(dto.toMono())
        whenever(ipAssignDemandService.cancelDemand(demandId)).thenReturn(dto.toMono()).thenReturn(dto.toMono())
        whenever(demandConverter.toIpAssignDemandCancelMessage(dto)).thenReturn(message.toMono())
        whenever(messagePublishService.publish(MessageType.IP_ASSIGN_DEMAND_CANCEL, message)).thenReturn(Unit.toMono())

        val result = target.cancelAssignIpDemand(issuerId, request).block()!!

        //then
        assertTrue(result.statusCode.is2xxSuccessful)
    }
}