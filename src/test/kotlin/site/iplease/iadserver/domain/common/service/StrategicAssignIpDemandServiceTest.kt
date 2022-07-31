package site.iplease.iadserver.domain.common.service

import TestDummyDataUtil
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.common.strategy.AcceptDemandStrategy
import site.iplease.iadserver.domain.common.strategy.AddDemandStrategy
import site.iplease.iadserver.domain.common.strategy.CancelDemandStrategy
import site.iplease.iadserver.domain.common.strategy.RejectDemandStrategy
import site.iplease.iadserver.global.common.data.dto.DemandDto

class StrategicAssignIpDemandServiceTest {
    private lateinit var addDemandStrategy: AddDemandStrategy
    private lateinit var cancelDemandStrategy: CancelDemandStrategy
    private lateinit var rejectDemandStrategy: RejectDemandStrategy
    private lateinit var acceptDemandStrategy: AcceptDemandStrategy
    private lateinit var target: StrategicIpAssignDemandService

    @BeforeEach
    fun beforeEach() {
        addDemandStrategy = mock()
        cancelDemandStrategy = mock()
        rejectDemandStrategy = mock()
        acceptDemandStrategy = mock()
        target = StrategicIpAssignDemandService(addDemandStrategy, cancelDemandStrategy, rejectDemandStrategy, acceptDemandStrategy)
    }

    //addDemand 로직
    //예약추가 트랜잭션을 수행한다.-> addDemandStrategy
    @Test @DisplayName("예약추가 성공 테스트")
    fun testAddDemandSuccess() {
        //예약추가에 성공하였다면, 추가한 예약정보를 반환한다.
        //예약추가가 성공하려면 아래의 작업이 성공해야한다.
        //- 예약추가 트랜잭션 수행

        //given
        val dto = mock<DemandDto>()
        val expectedResult = mock<DemandDto>()

        //when
        whenever(addDemandStrategy.addDemand(dto)).thenReturn(expectedResult.toMono())

        val result = target.addDemand(dto).block()!!

        //then
        assertEquals(expectedResult, result)
    }

    //cancelDemand 로직
    //예약취소 트랜잭션을 수행한다.-> cancelDemandStrategy
    @Test @DisplayName("예약취소 성공 테스트")
    fun testCancelDemandSuccess() {
        //예약취소에 성공하였다면, 취소한 예약정보를 반환한다.
        //예약취소가 성공하려면 아래의 작업이 성공해야한다.
        //- 예약취소 트랜잭션 수행

        //given
        val demandId = TestDummyDataUtil.id()
        val expectedResult = mock<DemandDto>()

        //when
        whenever(cancelDemandStrategy.cancelDemand(demandId)).thenReturn(expectedResult.toMono())

        val result = target.cancelDemand(demandId).block()!!

        //then
        assertEquals(expectedResult, result)
    }

    //rejectDemand 로직
    //예약거절 트랜잭션을 수행한다.-> rejectDemandStrategy
    @Test @DisplayName("예약거절 성공 테스트")
    fun testRejectDemandSuccess() {
        //예약거절에 성공하였다면, 거절한 예약정보를 반환한다.
        //예약거절이 성공하려면 아래의 작업이 성공해야한다.
        //- 예약거절 트랜잭션 수행

        //given
        val demandId = TestDummyDataUtil.id()
        val reason = TestDummyDataUtil.reason()
        val expectedResult = mock<DemandDto>()

        //when
        whenever(rejectDemandStrategy.rejectDemand(demandId, reason)).thenReturn(expectedResult.toMono())

        val result = target.rejectDemand(demandId, reason).block()!!

        //then
        assertEquals(expectedResult, result)
    }

    //acceptDemand 로직
    //예약수락 트랜잭션을 수행한다.-> acceptDemandStrategy
    @Test @DisplayName("예약수락 성공 테스트")
    fun testAcceptDemandSuccess() {
        //예약수락에 성공하였다면, 수락한 예약정보를 반환한다.
        //예약수락이 성공하려면 아래의 작업이 성공해야한다.
        //- 예약수락 트랜잭션 수행

        //given
        val demandId = TestDummyDataUtil.id()
        val assignIp = TestDummyDataUtil.assignIp()
        val expectedResult = mock<DemandDto>()

        //when
        whenever(acceptDemandStrategy.acceptDemand(demandId, assignIp)).thenReturn(expectedResult.toMono())

        val result = target.acceptDemand(demandId, assignIp).block()!!

        //then
        assertEquals(expectedResult, result)
    }
}