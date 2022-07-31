package site.iplease.iadserver.domain.accept.subscriber

import TestDummyDataUtil
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.accept.strategy.DemandAcceptCompensateStrategy
import site.iplease.iadserver.domain.accept.util.DemandAcceptedErrorOnManageConverter
import site.iplease.iadserver.global.accept.data.dto.DemandAcceptedErrorOnManageDto
import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptedErrorOnManageMessage

class IpAssignDemandAcceptedErrorOnManageMessageSubscriberV1Test {
    private lateinit var demandAcceptedErrorOnManageConverter: DemandAcceptedErrorOnManageConverter
    private lateinit var demandAcceptCompensateStrategy: DemandAcceptCompensateStrategy
    private lateinit var target: IpAssignDemandAcceptedErrorOnManageMessageSubscriberV1

    @BeforeEach
    fun before() {
        demandAcceptedErrorOnManageConverter = mock()
        demandAcceptCompensateStrategy = mock()
        target = IpAssignDemandAcceptedErrorOnManageMessageSubscriberV1(demandAcceptedErrorOnManageConverter, demandAcceptCompensateStrategy)
    }

    //subscribe 로직
    //오류정보를 추출한다.
    //신청수락 보상트랜잭션을 수행한다.

    @Test @DisplayName("IP할당신청수락됨오류처리 성공 테스트")
    fun testSubscribeSuccess() {
        //신청수락됨오류처리에 성공하려면 아래의 작업이 성공해야한다.
        //- 신청수락 보상트랜잭션 수행

        //given
        val demandId = TestDummyDataUtil.id()
        val originStatus = TestDummyDataUtil.demandStatus()
        val issuerId = TestDummyDataUtil.id()
        val demandIssuerId = TestDummyDataUtil.id()
        val title = TestDummyDataUtil.title()
        val description = TestDummyDataUtil.description()
        val usage = TestDummyDataUtil.usage()
        val expireAt = TestDummyDataUtil.randomDate()
        val assignIp = TestDummyDataUtil.assignIp()
        val message = TestDummyDataUtil.errorMessage()

        val errorMessage = IpAssignDemandAcceptedErrorOnManageMessage(demandId, originStatus, issuerId, demandIssuerId, title, description, usage, expireAt, assignIp, message)
        val dto = mock<DemandAcceptedErrorOnManageDto>()

        //when
        whenever(demandAcceptedErrorOnManageConverter.convert(errorMessage)).thenReturn(dto.toMono())
        whenever(demandAcceptCompensateStrategy.compensate(dto)).thenReturn(Unit.toMono())

        target.subscribe(errorMessage)

        //then
        verify(demandAcceptCompensateStrategy, times(1)).compensate(dto)
    }
}