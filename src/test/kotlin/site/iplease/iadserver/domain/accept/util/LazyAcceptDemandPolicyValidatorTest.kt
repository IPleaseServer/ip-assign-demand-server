package site.iplease.iadserver.domain.accept.util

import TestDummyDataUtil
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.accept.repository.AcceptedDemandRepository
import site.iplease.iadserver.domain.reject.repository.RejectedDemandRepository
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.data.type.DemandPolicyType
import site.iplease.iadserver.global.common.util.DemandPolicyValidator

class LazyAcceptDemandPolicyValidatorTest {
    private lateinit var demandPolicyValidator: DemandPolicyValidator
    private lateinit var acceptedDemandRepository: AcceptedDemandRepository
    private lateinit var rejectedDemandRepository: RejectedDemandRepository
    private lateinit var target: LazyAcceptDemandPolicyValidator

    @BeforeEach
    fun beforeEach() {
        demandPolicyValidator = mock()
        acceptedDemandRepository = mock()
        rejectedDemandRepository = mock()
        target = LazyAcceptDemandPolicyValidator(demandPolicyValidator, acceptedDemandRepository, rejectedDemandRepository)
    }

    //validate 로직
    //기존 정책을 검사한다.
    //만약 수락정책에 대한 검사라면 추가정책을 검사한다.

    @Test @DisplayName("IP할당신청수락정책 검사 성공 테스트")
    fun testValidate() {
        //수락정책 검사에 성공하였다면, 신청정보를 반환한다.
        //수락정책 검사에 성공하려면 아래의 작업이 성공해야한다.
        //- 신청수락정책 검사 트랜잭션 수행

        //given
        val demandId = TestDummyDataUtil.id()
        val dto = mock<DemandDto>()
        val expectedResult = mock<DemandDto>()

        //when
        whenever(expectedResult.id).thenReturn(demandId)
        whenever(demandPolicyValidator.validate(dto, DemandPolicyType.DEMAND_ACCEPT)).thenReturn(expectedResult.toMono())
        whenever(acceptedDemandRepository.exist(demandId)).thenReturn(false.toMono())
        whenever(rejectedDemandRepository.exist(demandId)).thenReturn(false.toMono())

        val result = target.validate(dto, DemandPolicyType.DEMAND_ACCEPT).block()!!

        //then
        assertEquals(result, expectedResult)
    }
}