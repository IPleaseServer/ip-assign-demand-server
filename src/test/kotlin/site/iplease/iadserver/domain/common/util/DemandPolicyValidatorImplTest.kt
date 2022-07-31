package site.iplease.iadserver.domain.common.util

import DemandTestUtil
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
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.data.type.DemandPolicyType
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.global.common.util.DateUtil
import java.time.LocalDate

class DemandPolicyValidatorImplTest {
    private lateinit var demandRepository: DemandRepository
    private lateinit var dateUtil: DateUtil
    private lateinit var target: DemandPolicyValidatorImpl
    private lateinit var randomDto: DemandDto

    @BeforeEach
    fun beforeEach() {
        demandRepository = mock()
        dateUtil = mock()
        target = DemandPolicyValidatorImpl(demandRepository, dateUtil)

        val demandId = TestDummyDataUtil.id()
        val issuerId = TestDummyDataUtil.id()
        val title = TestDummyDataUtil.title()
        val description = TestDummyDataUtil.description()
        val usage = TestDummyDataUtil.usage()
        val expireAt = TestDummyDataUtil.randomDate()
        randomDto = DemandDto(demandId, issuerId, title, description, usage, expireAt)
    }

    //validateCancel 로직
    //예약 존재여부를 검사한다.
    //예약 소유여부를 검사한다.
    //예약정보를 반환한다.

    @Test @DisplayName("IP할당예약취소정책 검사 성공 테스트")
    fun testValidateCancelSuccess() {
        //취소정책 검사에 성공하였다면, 예약정보를 반환한다.
        //취소정책 검사에 성공하려면 아래의 작업이 성공해야한다.
        //- 예약취소정책 검사 트랜잭션 수행

        //예약취소정책 검사 트랜잭션이 성공하려면, 아래의 조건을 만족해야한다.
        //- 예약이 존재할 것
        //- issuer가 예약의 issuer(소유자)일 것

        //given
        val dto = randomDto.copy()
        val entity = DemandTestUtil.toEntity(1, dto)

        //when
        whenever(demandRepository.existsByIdentifier(dto.id)).thenReturn(true.toMono())
        whenever(demandRepository.findByIdentifier(dto.id)).thenReturn(entity.toMono())

        val result = target.validate(dto, DemandPolicyType.DEMAND_CANCEL).block()!!

        //then
        assertEquals(result, dto)
        verify(demandRepository, times(1)).existsByIdentifier(dto.id)
        verify(demandRepository, times(1)).findByIdentifier(dto.id)
    }

    //validateCreate 로직
    //예약 만료기한을 검사한다.
    //예약 제목을 검사한다.
    //예약정보를 변조한다. (id = 0)
    //예약정보를 반환한다.

    @Test @DisplayName("IP할당예약추가정책 검사 성공 테스트")
    fun testValidateCreateSuccess() {
        //추가정책 검사에 성공하였다면, 예약정보를 반환한다.
        //추가정책 검사에 성공하려면 아래의 작업이 성공해야한다.
        //- 예약추가정책 검사 트랜잭션 수행

        //예약추가정책 검사 트랜잭션이 성공하려면, 아래의 조건을 만족해야한다.
        //- expireAt이 오늘 이후일 것
        //- title이 25자 이하일 것

        //given
        val now = mock<LocalDate>()
        val expireAt = mock<LocalDate>()
        val dto = randomDto.copy(expireAt = expireAt)

        //when
        whenever(dateUtil.dateNow()).thenReturn(now)
        whenever(expireAt.isAfter(now)).thenReturn(true)

        val result = target.validate(dto, DemandPolicyType.DEMAND_CREATE).block()!!

        //then
        assertEquals(result, dto.copy(id=0))
        verify(dateUtil, times(1)).dateNow()
        verify(expireAt, times(1)).isAfter(now)
    }

    //validateReject 로직
    //예약 존재여부를 검사한다.
    //예약정보를 반환한다.

    //TODO 예약거절정책에 수락된 예약에 존재하지 않을 것 조건추가하기
    @Test @DisplayName("IP할당예약거절정책 검사 성공 테스트")
    fun testValidateRejectSuccess() {
        //거절정책 검사에 성공하였다면, 예약정보를 반환한다.
        //거절정책 검사에 성공하려면 아래의 작업이 성공해야한다.
        //- 예약거절정책 검사 트랜잭션 수행

        //예약거절정책 검사 트랜잭션이 성공하려면, 아래의 조건을 만족해야한다.
        //- 예약이 존재할 것

        //given
        val dto = randomDto.copy()

        //when
        whenever(demandRepository.existsByIdentifier(dto.id)).thenReturn(true.toMono())

        val result = target.validate(dto, DemandPolicyType.DEMAND_REJECT).block()!!

        //then
        assertEquals(result, dto)
        verify(demandRepository, times(1)).existsByIdentifier(dto.id)
    }

    //validateAccept 로직
    //예약 존재여부를 검사한다.
    //예약정보를 반환한다.

    //TODO 예약수락정책에 거절된 예약에 존재하지 않을 것 조건추가하기
    @Test @DisplayName("IP할당예약수락정책 검사 성공 테스트")
    fun testValidateAcceptSuccess() {
        //수락정책 검사에 성공하였다면, 예약정보를 반환한다.
        //수락정책 검사에 성공하려면 아래의 작업이 성공해야한다.
        //- 예약수락정책 검사 트랜잭션 수행

        //예약수락정책 검사 트랜잭션이 성공하려면, 아래의 조건을 만족해야한다.
        //- 예약이 존재할 것

        //given
        val dto = randomDto.copy()

        //when
        whenever(demandRepository.existsByIdentifier(dto.id)).thenReturn(true.toMono())

        val result = target.validate(dto, DemandPolicyType.DEMAND_ACCEPT).block()!!

        //then
        assertEquals(result, dto)
        verify(demandRepository, times(1)).existsByIdentifier(dto.id)
    }
}