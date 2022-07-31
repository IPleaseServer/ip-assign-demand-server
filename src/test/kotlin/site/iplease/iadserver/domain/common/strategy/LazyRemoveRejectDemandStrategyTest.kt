package site.iplease.iadserver.domain.common.strategy

import TestDummyDataUtil
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.common.data.entity.Demand
import site.iplease.iadserver.domain.reject.data.entity.RejectedDemand
import site.iplease.iadserver.domain.reject.repository.RejectedDemandRepository
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.data.type.DemandPolicyType
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.global.common.util.DemandConverter
import site.iplease.iadserver.global.common.util.DemandPolicyValidator

class LazyRemoveRejectDemandStrategyTest {
    private lateinit var demandRepository: DemandRepository
    private lateinit var demandConverter: DemandConverter
    private lateinit var rejectedDemandRepository: RejectedDemandRepository
    private lateinit var demandPolicyValidator: DemandPolicyValidator
    private lateinit var target: LazyRemoveRejectDemandStrategy

    @BeforeEach
    fun beforeEach() {
        demandRepository = mock()
        demandConverter = mock()
        rejectedDemandRepository = mock()
        demandPolicyValidator = mock()
        target = LazyRemoveRejectDemandStrategy(demandConverter, demandRepository, rejectedDemandRepository, demandPolicyValidator)
    }

    //rejectDemand 로직
    //예약정보를 추출한다. (byId)
    //예약정보를 검증한다.
    //예약 엔티티를 조회한다.
    //예약정보를 추출한다. (byEntity)
    //거절된 예약 엔티티를 추출한다.
    //거절된 예약 엔티티를 추가한다.
    //예약정보를 반환한다.

    @Test @DisplayName("IP할당예약거절 성공 테스트")
    fun testRejectDemandSuccess() {
        //예약거절이 성공하였다면, 거절한 예약정보를 반환한다.
        //예약거절이 성공하려면, 아래의 작업이 성공해야한다.
        //- 예약거절 트랜잭션 수행 (거절된 예약 추가)

        //given
        val demandId = TestDummyDataUtil.id()
        val dto = mock<DemandDto>()
        val entity = mock<Demand>()
        val reason = TestDummyDataUtil.reason()
        val rejectedDemand = RejectedDemand(demandId, reason)
        val expectedResult = mock<DemandDto>()

        //when
        whenever(demandConverter.toDto(demandId)).thenReturn(dto.toMono())
        whenever(demandPolicyValidator.validate(dto, DemandPolicyType.DEMAND_REJECT)).thenReturn(dto.toMono())
        whenever(demandRepository.findByIdentifier(demandId)).thenReturn(entity.toMono())

        whenever(demandConverter.toDto(entity)).thenReturn(expectedResult.toMono())
        whenever(expectedResult.id).thenReturn(demandId)
        whenever(rejectedDemandRepository.insert(rejectedDemand)).thenReturn(Unit.toMono().then())

        val result = target.rejectDemand(demandId, reason).block()!!

        //then
        assertEquals(result, expectedResult)
        verify(rejectedDemandRepository, times(1)).insert(rejectedDemand)
    }
}